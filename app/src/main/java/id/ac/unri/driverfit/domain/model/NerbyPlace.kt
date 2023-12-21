package id.ac.unri.driverfit.domain.model

import id.ac.unri.driverfit.data.remote.payload.Place


data class NerbyPlace(
    val nextPageToken: String,
    val place: List<Place>,
)
