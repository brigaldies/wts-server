package com.infiniteintelligence.wts.domain.threat

import com.infiniteintelligence.wts.domain.codes.Code

class ThreatTypeCode extends Code {
    static enum eValue {
        STORM_HAIL,
        STORM_FLOOD,
        TEMPERATURE_THRESHOLD_LOW,
        TEMPERATURE_THRESHOLD_HIGH
    }
}
