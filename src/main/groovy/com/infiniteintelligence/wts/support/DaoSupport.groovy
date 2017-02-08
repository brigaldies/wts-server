package com.infiniteintelligence.wts.support

import grails.validation.ValidationException

/**
 * In the spirit of DRY (Don't Repeat Yourself), this support class provides common DB I/O operations for domain classes.
 */
abstract class DaoSupport {

    /**
     * Saves a new domain class object, or updates an existing domain class object.
     * @param domainObj The domain object to save or update.
     * @param flush Optional (false by default) to specify whether the Hibernate session should be flushed.
     * @param failOnError Optional (false by default) to specify whether the save generates a ValidationException when one or more validations fail.
     * @return The saved or updated domain object.
     * @throws ValidationException when one or more validations fail and the parameter failOnError is true.
     */
    def saveObject(domainObj, boolean flush = false, boolean failOnError = false) {
        try {
            log.debug "saveObject(flush: $flush, failOnError: $failOnError): Begin..."
            def saved = null
            if (domainObj) {
                saved = domainObj.save(flush: flush, failOnError: failOnError)
                if (!saved) {
                    log.error "save() returned null."
                    domainObj.errors.each {
                        log.error it
                    }
                } else {
                    log.debug "Saved: $saved"
                }
            } else {
                def errMsg = "save(): 'domainObj' arg is null."
                log.error errMsg
                throw new IllegalArgumentException(errMsg)
            }

            // Return the saved object
            saved
        } finally {
            log.debug "saveObject(flush: $flush, failOnError: $failOnError): End."
        }
    }
}
