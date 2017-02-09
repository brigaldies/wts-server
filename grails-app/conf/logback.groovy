import ch.qos.logback.core.*
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

String applicationName = Metadata.current.'info.app.name'
String applicationVersion = Metadata.current.'info.app.version'
String logFileName = "${applicationName}-${applicationVersion}"
println "Using log file: ${logFileName}.log"

appender("APPLICATION", RollingFileAppender) {
    file = "${logFileName}.log"
    encoder(PatternLayoutEncoder) {
        Pattern = "%d %5p [%15.15t] %mdc %-40.40logger{39} : %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        FileNamePattern = "${logFileName}.%d{yyyy-MM-dd}.log"
    }
}

// Application's bootstrap
logger 'wts.server.BootStrap', DEBUG

// Spring Security
logger 'org.springframework.security', DEBUG
logger 'grails.plugin.springsecurity', TRACE
logger 'grails.plugin.springsecurity.rest', TRACE

// Hibernate
// logger 'org.hibernate.SQL', DEBUG
// logger 'org.hibernate.type.descriptor.sql.BasicBinder', TRACE

// Application
logger 'com.infiniteintelligence.wts', DEBUG

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    root(ERROR, ['STDOUT', 'FULL_STACKTRACE', 'APPLICATION'])
}
else {
    root(ERROR, ['STDOUT', 'APPLICATION'])
}