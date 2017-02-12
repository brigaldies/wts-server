package com.infiniteintelligence.wts.domain.threat

import com.infiniteintelligence.wts.domain.codes.Code

class ThreatSeverityCode extends Code {
    static enum eValue {
        INFO,
        WARNING,
        ALERT
    }
}
