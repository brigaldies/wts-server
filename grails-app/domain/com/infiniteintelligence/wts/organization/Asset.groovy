package com.infiniteintelligence.wts.organization

class Asset {

    Organization organization
    AssetTypeCode assetType

    static mapping = {
        assetType column: 'asset_type_cd'
    }

    static constraints = {
    }
}
