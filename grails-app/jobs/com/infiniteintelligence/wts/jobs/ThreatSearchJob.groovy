package com.infiniteintelligence.wts.jobs

import com.infiniteintelligence.wts.domain.organization.Asset
import com.infiniteintelligence.wts.domain.organization.Organization
import com.infiniteintelligence.wts.services.threat.ThreatService
import groovy.util.logging.Slf4j

@Slf4j
class ThreatSearchJob {

    ThreatService threatService

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
        simple startDelay: 10000, repeatInterval: 60000 // execute job once every 60 secs
    }

    /**
     * Threat job execution method
     * @return
     */
    def execute() {
        // execute job
        threatService.scan()
    }
}
