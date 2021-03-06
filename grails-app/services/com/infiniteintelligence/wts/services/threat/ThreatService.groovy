package com.infiniteintelligence.wts.services.threat

import com.infiniteintelligence.wts.domain.organization.Address
import com.infiniteintelligence.wts.domain.organization.Asset
import com.infiniteintelligence.wts.domain.organization.Organization
import com.infiniteintelligence.wts.domain.security.User
import com.infiniteintelligence.wts.domain.threat.Threat
import com.infiniteintelligence.wts.domain.threat.ThreatSeverityCode
import com.infiniteintelligence.wts.domain.threat.ThreatTypeCode
import com.infiniteintelligence.wts.services.crud.CrudService
import com.infiniteintelligence.wts.services.search.SearchService
import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovy.util.logging.Slf4j
import org.grails.web.json.JSONElement

@Slf4j
class ThreatService {

    // Annotate each method that needs a DB transaction
    static transactional = false

    // Injected services
    SearchService searchService
    CrudService crudService

    /**
     * Threats scan for all organizations and their assets
     */
    @Transactional
    void scan() {
        User systemUser = User.findByUsername('system')
        assert systemUser
        Organization.list([sort: 'name']).each { Organization organization ->
            log.info "Searching threats for organization ${organization.name}..."
            Asset.findByOrganization(organization, [sort: 'id']).each { Asset asset ->
                log.info "Searching threats for assert type ${asset.assetType} at location ${asset.address}..."
                // Date and time filter
                String queryDateAndTime = 'FCTTIME.epoch_dt:[' + new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone.getTimeZone('GMT')) + ' TO *]'

                // Location filter
                String queryLocation = 'parent_s:*' + (asset.address.state ?: asset.address.province) + '/' + asset.address.city + '*'

                // Temperature filter
                String queryTemperatureRange = '(temp.english:[* TO ' + "${asset.temperatureThresholdLow}" + '] OR temp.english:[' + "${asset.temperatureThresholdHigh}" + ' TO *])'

                // Final query
                String query = queryDateAndTime + ' AND ' + queryLocation + ' AND ' + queryTemperatureRange

                // Search!
                JSONElement searchResults = searchService.search(
                        'weather', // Data source
                        query // Search query (Solr syntax)
                )

                if (!searchResults.response) {
                    log.info "No search results!"
                } else {
                    log.info "Search docs: ${searchResults.response.numFound}"
                    searchResults.response.docs.each { doc ->
                        String location = (doc.parent_s as String).tokenize('/').last()
                        String epoch = doc."FCTTIME.epoch_dt"
                        Integer temperatureAlert = doc."temp.english" as Integer
                        log.info "location=${location}, epoch=${epoch}, temp=${temperatureAlert}"

                        // Persist the threat
                        Date dateAlertBegin = new Date().parse("yyyy-MM-dd'T'HH:mm:ss'Z'", epoch, TimeZone.getTimeZone('GMT'))
                        Date dateAlertEnd = null
                        use(TimeCategory) {
                            // Since the weather feed provide hourly temperatures, an alert is assumed to last an hour only.
                            // The next temperature estimate will either sustain or end the threat.
                            dateAlertEnd = dateAlertBegin + 1.hours
                        }
                        ThreatTypeCode threatTypeCode
                        if (temperatureAlert >= asset.temperatureThresholdHigh) {
                            threatTypeCode = ThreatTypeCode.get(ThreatTypeCode.eValue.TEMPERATURE_THRESHOLD_HIGH.name())
                        } else if (temperatureAlert <= asset.temperatureThresholdLow) {
                            threatTypeCode = ThreatTypeCode.get(ThreatTypeCode.eValue.TEMPERATURE_THRESHOLD_LOW.name())
                        }
                        if (threatTypeCode) {
                            Threat threat = crudService.create(
                                    new Threat(
                                            asset: asset,
                                            threatType: threatTypeCode,
                                            threatSeverity: ThreatSeverityCode.get(ThreatSeverityCode.eValue.ALERT.name()),
                                            temperatureAlert: temperatureAlert,
                                            dateBegin: dateAlertBegin,
                                            dateEnd: dateAlertEnd,
                                            lastUpdatedByUser: systemUser,
                                            lastUpdatedComment: query
                                    )
                            ) as Threat
                            assert threat
                        } else {
                            log.error "No actual threat!" // Should not happen by construction of the search query.
                        }
                    }
                }
            } // Asset
        } // Organization
    } // scan()

    @Transactional(readOnly = true)
    List<Threat> list(Map params) {
        List<Threat> threats = Threat.withCriteria {
            if (params.forecast) {
                Date dateFrom = new Date()
                Date dateTo
                use(TimeCategory) {
                    dateTo = dateFrom + (params.forecast as Integer).days
                }
                log.info "dateFrom=${dateFrom}, dateTo=${dateTo}"
                between('dateBegin', dateFrom, dateTo)
            }
            order('dateBegin', 'asc')
        } as Threat[]
        // List<Threat> threats = Threat.list([sort: 'dateBegin', order: 'asc'])
        log.debug "${threats.size()} threat(s) retrieved"
        threats
    }

    /**
     * Returns a map of threats information
     * <city name>: Array of threats
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    Map<String, List<Threat>> getThreatsByLocation(Map params) {
        Date dateFrom = null
        Date dateTo = null
        String whereClause = ''
        if (params?.forecast) {
            dateFrom = new Date()
            dateTo
            use(TimeCategory) {
                dateTo = dateFrom + (params.forecast as Integer).days
            }
            log.info "dateFrom=${dateFrom}, dateTo=${dateTo}"
            whereClause = "and t.dateBegin between :dateFrom and :dateTo"
        }
        List<Address> locations = Address.executeQuery("""
select distinct addr from Address addr, Asset a, Threat t 
where addr = a.address and a = t.asset 
${whereClause}
order by addr.city asc
""", (dateTo && dateFrom) ? [dateFrom: dateFrom, dateTo: dateTo] : [])
        log.info "Retrieved ${locations.size()} distinct locations: ${locations*.city}"
        Map<String, List<Threat>> threatsByLocation = new HashMap<String, List<Threat>>()
        locations.each { Address address ->
            List<Threat> threats = Threat.executeQuery("""
select t from Threat t, Asset a, Address addr
where t.asset = a and a.address = addr and addr.city = :city
${whereClause}
order by t.dateBegin asc
""", (dateTo && dateFrom) ? [city: address.city, dateFrom: dateFrom, dateTo: dateTo] : [city: address.city])
            threatsByLocation."${address.city}" = threats
        }

        // Return the map of threats by location
        threatsByLocation
    }


    @Transactional(readOnly = true)
    Threat get(Long id) {
        Threat.get(id)
    }
}
