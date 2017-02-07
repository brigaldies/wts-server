package com.infiniteintelligence.wts.incident

class IncidentStatusAudit {

    Incident incident
    IncidentStatusCode status
    Date dateCreated

    static mapping = {
        status column: 'status_cd'
    }

    static constraints = {
    }
}
