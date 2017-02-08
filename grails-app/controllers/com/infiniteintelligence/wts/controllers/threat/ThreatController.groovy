package com.infiniteintelligence.wts.controllers.threat

import com.infiniteintelligence.wts.domain.threat.Threat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ThreatController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
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
