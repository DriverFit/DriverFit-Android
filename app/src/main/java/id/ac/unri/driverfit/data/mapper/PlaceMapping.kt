package id.ac.unri.driverfit.data.mapper

import id.ac.unri.driverfit.data.remote.payload.NearbySearchResponse
import id.ac.unri.driverfit.domain.model.NerbyPlace

fun NearbySearchResponse.asModel(): NerbyPlace {
    return NerbyPlace(
        nextPageToken = nextPageToken,
        place = places
    )
}
