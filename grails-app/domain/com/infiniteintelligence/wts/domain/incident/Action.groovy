package com.infiniteintelligence.wts.domain.incident

import com.infiniteintelligence.wts.domain.BaseEntity
import com.infiniteintelligence.wts.domain.security.User

class Action extends BaseEntity {

    Incident incident
    Integer stepNumber
    ActionStatusCode status
    User assignedUser

    static mapping = {
        status column: 'status_cd'
    }

    static constraints = {
    }
}
