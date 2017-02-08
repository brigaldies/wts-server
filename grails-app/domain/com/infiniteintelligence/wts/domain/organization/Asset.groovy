package com.infiniteintelligence.wts.domain.organization

import com.infiniteintelligence.wts.domain.BaseEntity

class Asset extends BaseEntity {

    Organization organization
    AssetTypeCode assetType

    static mapping = {
        assetType column: 'asset_type_cd'
    }

    static constraints = {
    }
}
