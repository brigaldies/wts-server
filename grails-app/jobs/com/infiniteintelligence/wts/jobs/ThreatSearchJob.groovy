package com.infiniteintelligence.wts.jobs

import com.infiniteintelligence.wts.services.search.solr.SolrService
import groovy.util.logging.Slf4j

@Slf4j
class ThreatSearchJob {

    SolrService solrService

    // No hibernate session bound. Database writes are executed via service calls when needed.
    def sessionRequired = false

    // Not re-entrant, i.e., the next scheduled execution waits until the current execution completes.
    def concurrent = false

    // Schedule group
    def group = 'Threat Search'

    // Job's description.
    // TODO: Use the description field to hold the parameter name: It is used to set the job's trigger (See JobService.scheduleApplicationJobs)
    def description = 'Threat Search'

    static triggers = {
      simple repeatInterval: 60000 // execute job once every 60 secs
    }

    def execute() {
        // execute job
        solrService.search('solr', 'json')
    }
}
