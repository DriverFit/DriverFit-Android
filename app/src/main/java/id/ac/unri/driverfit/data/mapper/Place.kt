package id.ac.unri.driverfit.data.mapper

import id.ac.unri.driverfit.data.remote.payload.NearbySearchResponse
import id.ac.unri.driverfit.data.remote.payload.PlaceResponse
import id.ac.unri.driverfit.domain.model.Place

fun NearbySearchResponse.Place.asModel(): Place {
    return Place(
        id = placeId,
        lat = geometry.location.lat,
        lng = geometry.location.lng,
        name = name,
        rating = rating,
        vicinity = vicinity,
        phoneNumber = null,
        businessStatus = businessStatus
    )
}

fun PlaceResponse.asModel(): Place {
    return Place(
        id = id ?: "",
        lat = lat ?: 0.0,
        lng = lng ?: 0.0,
        name = name ?: "",
        rating = rating?.toFloat() ?: 0F,
        vicinity = address ?: "",
        phoneNumber = phoneNumber,
        businessStatus = businessStatus ?: ""
    )
}
