package com.infiniteintelligence.wts.threat

class Threat {

    ThreatTypeCode threatType // e.g., Hail
    ThreatSeverityCode threatSeverity // e.g., Warning, Alert
    Date dateCreated // When the threat is identified or predicted
    Date dateBegin // When the threat starts
    Date dateEnd  // When the threat ends

    static mapping = {
        threatType column: 'threat_type_cd'
        threatSeverity column: 'threat_severity_cd'
    }

    static constraints = {
    }
}
