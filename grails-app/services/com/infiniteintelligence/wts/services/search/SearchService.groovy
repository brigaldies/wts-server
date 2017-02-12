package com.infiniteintelligence.wts.services.search

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.web.http.HttpHeaders
import groovy.util.logging.Slf4j
import org.grails.web.json.JSONElement

/**
 * Solr search service
 */
@Slf4j
class SearchService {

    // Annotate each method that needs a DB transaction
    static transactional = false

    // TODO: Use an external parameter
    static solrCollection = 'gettingstarted'
    static solrUrl = 'http://localhost:8983/solr/' + solrCollection + '/select'

    static String securityToken
    static long securityTokenSysTime

    /**
     * Search
     * @param term Term to search
     * @param format Solr query results format (e.g., json)
     * @return
     */
    def search0(String term, String format) {

        authenticate()

        /*
        String restUrl = solrUrl + '?indent=on&q=' + term + '&wt=' + format

        log.info "Calling ${restUrl}..."

        HttpURLConnection connection = new URL(restUrl).openConnection() as HttpURLConnection

        // set some headers
        connection.setRequestProperty('User-Agent', 'groovy-2.4.7')
        connection.setRequestProperty('Accept', 'application/json')

        // get the response code - automatically sends the request
        // println connection.responseCode + ": " + connection.inputStream.text

        if (connection.responseCode == 200) {
            // get the JSON response
            def json = connection.inputStream.withCloseable { inStream ->
                new JsonSlurper().parse(inStream as InputStream)
            }

            // extract some data from the JSON, printing a report
            json.response.docs.each { doc ->
               log.info "Doc id=${doc.id}"
            }
        } else {
            log.error connection.responseCode + ": " + connection.inputStream.text
        }
*/
    }

    /**
     * Authenticate to the Fusion REST API.
     * @return Security token
     */
    String authenticate() {
        // TODO: Externalize the Fusion authenticate URL as a deployment parameter
        // Fusion security realm "native" corresponds to Fusion's locally managed users.
        String authUrl = 'http://localhost:8764/api/session?realmName=native'

        // Refresh the Fusion REST API security token every 5 mins (Max TTL is 10 mins)
        if (!securityToken || (System.currentTimeMillis() - securityTokenSysTime) > 5 * 60 * 1000) {

            log.info "Authenticating to ${authUrl}..."

            RestBuilder restBuilder = new RestBuilder()
            RestResponse response = restBuilder.post(authUrl) {
                json {
                    username = 'admin'
                    password = 'fusion2017'
                }
            }

            log.info "Status: ${response.getStatus()}"
            response.getHeaders().each { header ->
                log.info "${header}"
            }
            List<String> cookie = response.getHeaders().get(HttpHeaders.SET_COOKIE)
            log.info "SET_COOKIE: ${cookie}, #entries=${cookie.size()}"
            String cookieId = cookie.find { it.contains('id=') }
            log.info "cookie line: ${cookieId}"
            securityToken = cookieId.tokenize(';').find { it.startsWith('id=') }
            securityTokenSysTime = System.currentTimeMillis()
            log.info "cookie: ${securityToken} obtained on ${new Date(securityTokenSysTime)}"
        } else {
            log.info "Using cookie: ${securityToken}"
        }
        return securityToken
    }

    /**
     * Execute a search via the Fusion REST API
     * @param collection
     * @param query
     * @return The search results' JSON structure as a JSONElement
     */
    JSONElement search(String collection, String query) {

        // sort=FCTTIME.epoch_dt asc&start=0&q=*:*&debug=true&rows=10
        String searchUrl = 'http://localhost:8764/api/apollo/solr/' + collection +
                '/select?q=' + query +
                '&sort=FCTTIME.epoch_dt asc&wt=json&rows=100'

        log.info "Searching: GET ${searchUrl}"

        authenticate()

        RestBuilder restBuilder = new RestBuilder()
        RestResponse response = restBuilder.get(searchUrl) {
            header 'Cookie', securityToken
        }

        // TODO: Handle errors...

        return response.json
    }
}
