package com.infiniteintelligence.wts.incident

import com.infiniteintelligence.wts.security.User

class Action {

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
