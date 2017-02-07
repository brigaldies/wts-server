package com.infiniteintelligence.wts.security

import com.infiniteintelligence.wts.organization.Organization

class User extends Principal {

    String firstName
    String lastName
    String email
    String phone
    User report
    Organization organization

    Date dateCreated
    Date lastUpdated

    static constraints = {
        firstName blank: false
        lastName blank: false
        email blank: false, email: true
        phone nullable: true, blank: false
        report nullable: true
        organization nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }
}