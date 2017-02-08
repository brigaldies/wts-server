package com.infiniteintelligence.wts.domain.incident

import com.infiniteintelligence.wts.domain.BaseEntity
import com.infiniteintelligence.wts.domain.security.User


class IncidentTeamMember extends BaseEntity {

    Incident incident
    User user

    static constraints = {
    }
}
