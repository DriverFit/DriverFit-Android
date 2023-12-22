package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.model.NerbyPlace
import id.ac.unri.driverfit.domain.repository.GoogleMapsRepository

class GetRestPlaceUseCase(
    private val googleMapsRepository: GoogleMapsRepository
) {

    suspend operator fun invoke(
        lat: Double,
        lng: Double,
        radius: Double = 5 * 1500.0,
        keyword: String = "hotel",
        pageToken: String? = null
    ): NerbyPlace = googleMapsRepository.getNearby(lat, lng, radius, keyword, pageToken)

}
