package id.ac.unri.driverfit.domain.usecase


import id.ac.unri.driverfit.data.remote.payload.Place
import id.ac.unri.driverfit.domain.repository.GoogleMapsRepository

class GetPlaceUseCase(
    private val googleMapsRepository: GoogleMapsRepository
) {

    suspend operator fun invoke(placeId: String): Result<Place> = runCatching {
        TODO()
    }
}
