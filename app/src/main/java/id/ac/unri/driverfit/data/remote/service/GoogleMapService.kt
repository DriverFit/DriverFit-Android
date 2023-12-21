package id.ac.unri.driverfit.data.remote.service

import id.ac.unri.driverfit.data.remote.payload.NearbySearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapService {

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=")
    suspend fun getNearby(
        @Query("location")
        location: String?,
        @Query("radius")
        radius: Double?,
        @Query("type")
        type: String?,
        @Query("keyword")
        keyword: String?
    ): Response<NearbySearchResponse>
}
