package com.infiniteintelligence.wts.domain.incident

import com.infiniteintelligence.wts.domain.BaseEntity

class IncidentStatusAudit extends BaseEntity {

    Incident incident
    IncidentStatusCode status
    Date dateCreated

    static mapping = {
        status column: 'status_cd'
    }

    static constraints = {
    }
}
