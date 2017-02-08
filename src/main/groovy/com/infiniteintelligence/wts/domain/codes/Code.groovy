package com.infiniteintelligence.wts.domain.codes

import groovy.transform.EqualsAndHashCode

/**
 * Base class to share fields and constraints for code tables composed of a code and name.
 */
@EqualsAndHashCode(includes = ['code'])
abstract class Code implements Comparable {

    String code
    String label
    String description
    Boolean enabled
    Date dateCreated // Date the code was created (Managed by GORM)

    static mapping = {
        id name: 'code', generator: 'assigned' // 'code' is the table's primary key
        cache usage: 'read-only'
        code updateable: false
        description length: 4000
        enabled defaultValue: 1
        // The generated DDL will have a default value of �1�, e.g., enabled number(1,0) default 1 not null. Note: Grails maps Boolean into number(1,0) in Oracle.
    }

    static constraints = {
        code blank: false
        label blank: false // , unique: true
        description nullable: true
    }

    int compareTo(Object o) {
        return code.compareTo(o.code)
    }

    String getCodeAndLabel() {
        "$code - $label"
    }

    String toString() {
        getCodeAndLabel()
    }
}
