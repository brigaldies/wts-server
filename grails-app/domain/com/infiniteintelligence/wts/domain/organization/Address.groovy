package com.infiniteintelligence.wts.domain.organization

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode(includes = ['id'])
class Address {

    String street1
    String street2
    String city
    String state
    String province
    String country
    BigDecimal latitute
    BigDecimal longitute

    static constraints = {
        street2 nullable: true
        state nullable: true
        province nullable: true
    }
}
