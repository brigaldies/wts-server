package wts.server

import groovy.util.logging.Slf4j

@Slf4j
class BootStrap {

    def springSecurityService

    def init = { servletContext ->
        if (springSecurityService) {
            if (!springSecurityService?.passwordEncoder) {
                log.error 'BootStrap: No password encoder!?'
            } else {
                log.info "BootStrap: Password encoder is ${springSecurityService.passwordEncoder.class}"
            }
        } else {
            log.error 'BootStrap: No springSecurityService wired in!'
        }
    }
    def destroy = {
    }
}
