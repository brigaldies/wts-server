package com.infiniteintelligence.wts.controllers

import com.infiniteintelligence.wts.services.threat.ThreatService
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.*
import grails.converters.*
import groovy.util.logging.Slf4j

@Slf4j
@Secured('ROLE_USER')
class ThreatsController {
	static responseFormats = ['json']

    ThreatService threatService

    @Secured('ROLE_USER')
    Object index() {
        log.debug "ThreatsController.index(), params=${params}"
//        if (params.bylocation) {
//            [threatsByLocation: threatService.getThreatsByLocation()]
//        } else {
//            [threats: threatService.list(params)]
//        }

        [threats: threatService.list(params), threatsByLocation: threatService.getThreatsByLocation(params)]
    }

    @Secured('ROLE_USER')
    Object show() {
        log.debug "ThreatsController.show(), params=${params}"
        [threat: threatService.get(params.id as Long)]
    }
}
