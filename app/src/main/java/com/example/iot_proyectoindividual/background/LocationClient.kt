package com.example.iot_proyectoindividual.background

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval : Long): Flow<Location>

    class LocationException(message: String): Exception()
}