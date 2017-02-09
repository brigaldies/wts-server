package com.infiniteintelligence.wts.services.search.solr

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

/**
 * Solr search service
 */
@Slf4j
class SolrService {

    // Annotate each method that needs a DB transaction
    static transactional = false

    // TODO: Use an external parameter
    static solrCollection = 'gettingstarted'
    static solrUrl = 'http://localhost:8983/solr/' + solrCollection + '/select'

    /**
     * Search
     * @param term Term to search
     * @param format Solr query results format (e.g., json)
     * @return
     */
    def search(String term, String format) {

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

    }
}
