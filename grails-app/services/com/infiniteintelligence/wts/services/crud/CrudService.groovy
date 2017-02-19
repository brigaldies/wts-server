package com.infiniteintelligence.wts.services.crud

import com.infiniteintelligence.wts.support.DaoSupport
import grails.transaction.Transactional
import groovy.util.logging.Slf4j

@Slf4j
class CrudService extends DaoSupport {

    // Annotate each method that needs a DB transaction
    static transactional = false

    @Transactional
    Object create(Object obj) {
        saveObject(obj)
    }

    @Transactional
    def bulkDeleteAll(Class entityClass) {
        String hqlQuery = "delete from ${getClassName(entityClass)}"
        log.info "Executing ${hqlQuery}..."
        entityClass.executeUpdate(hqlQuery)
    }

    /**
     * Helper: Return the class name without the package.
     * @param clazz
     * @return
     */
    String getClassName(Class clazz) {
        clazz.name.substring(clazz.name.lastIndexOf('.') + 1)
    }
}
