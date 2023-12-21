package id.ac.unri.driverfit.domain.repository

import id.ac.unri.driverfit.data.remote.payload.Place
import id.ac.unri.driverfit.domain.model.NerbyPlace

interface GoogleMapsRepository {

    suspend fun getNearby(
        lat: Double,
        lng: Double,
        radius: Double,
        type: String,
        keyword: String,
        pageToken: String? = null
    ): NerbyPlace

    suspend fun getPlace(placeId: String): Place
}
