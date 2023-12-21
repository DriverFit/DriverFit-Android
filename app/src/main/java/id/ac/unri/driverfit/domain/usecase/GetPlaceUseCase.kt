package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.model.Place
import id.ac.unri.driverfit.domain.repository.GoogleMapsRepository

class GetPlaceUseCase(
    private val googleMapsRepository: GoogleMapsRepository
) {

    suspend operator fun invoke(placeId: String): Result<Place> = runCatching {
        googleMapsRepository.getPlace(placeId)
    }
}
