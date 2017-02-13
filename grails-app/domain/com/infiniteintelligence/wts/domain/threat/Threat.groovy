package com.infiniteintelligence.wts.domain.threat

import com.infiniteintelligence.wts.domain.BaseEntity
import com.infiniteintelligence.wts.domain.organization.Asset
import groovy.transform.ToString

@ToString
class Threat extends BaseEntity {

    Asset asset
    ThreatTypeCode threatType // e.g., Hail
    ThreatSeverityCode threatSeverity // e.g., Warning, Alert
    Integer temperatureAlert
    Date dateBegin // When the threat starts
    Date dateEnd  // When the threat ends

    static mapping = {
        threatType column: 'threat_type_cd'
        threatSeverity column: 'threat_severity_cd'
    }

    static constraints = {
        temperatureAlert nullable: true
    }
}
