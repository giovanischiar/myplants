package io.schiar.myplants

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by giovani on 10/06/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SensorData(val humidity: Int = 0, val illuminance: Int = 0)