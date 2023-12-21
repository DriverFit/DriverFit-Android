package id.ac.unri.driverfit.data

import id.ac.unri.driverfit.data.mapper.asModel
import id.ac.unri.driverfit.data.remote.GoogleMapRemoteDataSource
import id.ac.unri.driverfit.domain.model.Place
import id.ac.unri.driverfit.domain.repository.GoogleMapsRepository

class DefaultGoogleMapsRepository(
    private val googleMapRemoteDataSource: GoogleMapRemoteDataSource
) : GoogleMapsRepository {

    override suspend fun getNearby(
        lat: Double,
        lng: Double,
        radius: Double,
        type: String,
        keyword: String
    ): List<Place> {
        val res = googleMapRemoteDataSource.getNearby(lat, lng, radius, type, keyword)
        return res.map { it.asModel() }
    }

    override suspend fun getPlace(placeId: String): Place {
        return googleMapRemoteDataSource.getPlace(placeId).asModel()
    }
}
