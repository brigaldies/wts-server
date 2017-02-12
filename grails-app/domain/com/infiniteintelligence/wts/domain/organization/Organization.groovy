package com.infiniteintelligence.wts.domain.organization

import com.infiniteintelligence.wts.domain.BaseEntity
import groovy.transform.ToString

@ToString
class Organization extends BaseEntity {

    String name

    static constraints = {
    }
}
