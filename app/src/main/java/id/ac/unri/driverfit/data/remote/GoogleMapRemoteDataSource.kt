package id.ac.unri.driverfit.data.remote

import id.ac.unri.driverfit.data.remote.payload.NearbySearchResponse
import id.ac.unri.driverfit.data.remote.service.GoogleMapService


class GoogleMapRemoteDataSource(
    private val googleMapService: GoogleMapService
) {

    suspend fun getNearby(
        lat: Double,
        lng: Double,
        radius: Double,
        keyword: String?,
        pagetoken: String?
    ): NearbySearchResponse {
        val res = googleMapService.getNearby("$lat,$lng", radius, keyword, pagetoken)
        val data = res.takeIf { it.isSuccessful }?.body()
        if (data == null) {
            res.errorBody()?.let { throw RuntimeException(it.getErrorMessage()) }
        }

        return data ?: throw RuntimeException("Response body is empty")
    }
}
