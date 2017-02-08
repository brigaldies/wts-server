package com.infiniteintelligence.wts.controllers.threat

import com.infiniteintelligence.wts.domain.threat.Threat
import grails.plugin.springsecurity.annotation.Secured
import groovy.util.logging.Slf4j

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Slf4j
@Transactional(readOnly = true)
@Secured('ROLE_USER')
class ThreatsController {

    static responseFormats = ['json']
    // static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured('ROLE_USER')
    def index(Integer max) {
        log.debug "index: max=${max}"
        params.max = Math.min(max ?: 10, 100)
        respond Threat.list(params), model:[threatCount: Threat.count()]
    }

    def show(Threat threat) {
        respond threat
    }

    @Transactional
    def save(Threat threat) {
        if (threat == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (threat.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond threat.errors, view:'create'
            return
        }

        threat.save flush:true

        respond threat, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Threat threat) {
        if (threat == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (threat.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond threat.errors, view:'edit'
            return
        }

        threat.save flush:true

        respond threat, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Threat threat) {

        if (threat == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        threat.delete flush:true

        render status: NO_CONTENT
    }
}
