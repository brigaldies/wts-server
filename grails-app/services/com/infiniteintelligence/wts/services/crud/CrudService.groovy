package com.infiniteintelligence.wts.services.crud

import com.infiniteintelligence.wts.domain.codes.Code
import com.infiniteintelligence.wts.support.DaoSupport
import grails.transaction.Transactional

@Transactional
class CrudService extends DaoSupport {

    def create(Object obj) {
        saveObject(obj)
    }
}
