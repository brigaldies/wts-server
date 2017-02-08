package com.infiniteintelligence.wts.domain.organization

import com.infiniteintelligence.wts.domain.BaseEntity

class PersonAssetAffiliation extends BaseEntity {

    Person person
    Asset asset
    PersonAssetAffTypeCode affiliationType

    static mapping = {
        affiliationType column: 'affiliation_type_cd'
    }

    static constraints = {
    }
}
