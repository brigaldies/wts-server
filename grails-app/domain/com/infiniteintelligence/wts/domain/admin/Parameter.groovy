package com.infiniteintelligence.wts.domain.admin

import com.infiniteintelligence.wts.domain.BaseEntity
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ['name'])
class Parameter extends BaseEntity {

    String name
    String value
    String description

    static mapping = {
        id name: 'name', generator: 'assigned' // 'name' is the table's primary key
    }

    static constraints = {
        value blank: false
        description blank: false
        value nullable: true, maxSize: 4000
    }

    static enum eCatalog {

        JOB_CRON_THREAT_SEARCH,
    }
}
