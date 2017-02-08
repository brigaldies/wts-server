package com.infiniteintelligence.wts.domain.incident

import com.infiniteintelligence.wts.domain.BaseEntity
import com.infiniteintelligence.wts.domain.organization.Asset
import com.infiniteintelligence.wts.domain.security.User
import com.infiniteintelligence.wts.domain.threat.Threat

class Incident extends BaseEntity {

    Threat threat
    Asset asset
    User lead
    IncidentStatusCode status
    Date dateCreated
    Date lastUpdated

    static mapping = {
        status column: 'status_cd'
    }

    static constraints = {
    }
}
