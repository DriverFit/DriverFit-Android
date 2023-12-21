package id.ac.unri.driverfit.data

import id.ac.unri.driverfit.data.mapper.asModel
import id.ac.unri.driverfit.data.remote.GoogleMapRemoteDataSource
import id.ac.unri.driverfit.data.remote.payload.Place
import id.ac.unri.driverfit.domain.model.NerbyPlace
import id.ac.unri.driverfit.domain.repository.GoogleMapsRepository

class DefaultGoogleMapsRepository(
    private val googleMapRemoteDataSource: GoogleMapRemoteDataSource
) : GoogleMapsRepository {

    override suspend fun getNearby(
        lat: Double,
        lng: Double,
        radius: Double,
        type: String,
        keyword: String,
        pageToken: String?
    ): NerbyPlace {
        val res = googleMapRemoteDataSource.getNearby(lat, lng, radius, type, keyword, pageToken)
        return res.asModel()
    }

    override suspend fun getPlace(placeId: String): Place {
        TODO()
    }
}
