package com.infiniteintelligence.wts.domain.organization

import com.infiniteintelligence.wts.domain.BaseEntity
import groovy.transform.ToString

@ToString
class Asset extends BaseEntity {

    Organization organization
    AssetTypeCode assetType
    Address address
    Integer temperatureThresholdLow
    Integer temperatureThresholdHigh

    static mapping = {
        assetType column: 'asset_type_cd'
    }

    static constraints = {
        address nullable: true // For mobile asset
    }
}
