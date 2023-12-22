package id.ac.unri.driverfit.domain.repository

import id.ac.unri.driverfit.domain.model.NerbyPlace

interface GoogleMapsRepository {

    suspend fun getNearby(
        lat: Double,
        lng: Double,
        radius: Double,
        keyword: String,
        pageToken: String? = null
    ): NerbyPlace
}
