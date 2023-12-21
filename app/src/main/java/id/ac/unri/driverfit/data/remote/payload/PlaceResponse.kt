package id.ac.unri.driverfit.data.remote.payload

data class PlaceResponse(
    val id: String?,
    val name: String?,
    val rating: Double?,
    val address: String?,
    val phoneNumber: String?,
    val lat: Double?,
    val lng: Double?,
    val businessStatus: String?
)
