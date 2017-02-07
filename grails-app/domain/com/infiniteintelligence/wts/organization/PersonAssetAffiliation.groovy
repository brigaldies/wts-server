package com.infiniteintelligence.wts.organization

class PersonAssetAffiliation {

    Person person
    Asset asset
    PersonAssetAffTypeCode affiliationType

    static mapping = {
        affiliationType column: 'affiliation_type_cd'
    }

    static constraints = {
    }
}
