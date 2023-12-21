package id.ac.unri.driverfit.domain.repository

import id.ac.unri.driverfit.domain.model.Place

interface GoogleMapsRepository {

    suspend fun getNearby(
        lat: Double,
        lng: Double,
        radius: Double,
        type: String,
        keyword: String
    ): List<Place>

    suspend fun getPlace(placeId: String): Place
}
