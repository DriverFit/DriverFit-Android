package id.ac.unri.driverfit.data.remote.payload

import com.squareup.moshi.Json

data class NearbySearchResponse(
    @Json(name = "next_page_token")
    val nextPageToken: String?,

    @Json(name = "html_attributions")
    val htmlAttributions: List<Any?>? = null,

    @Json(name = "results")
    val places: List<Place>,

    @Json(name = "status")
    val status: String? = null


)

data class PhotosItem(

    @Json(name = "photo_reference")
    val photoReference: String? = null,

    @Json(name = "width")
    val width: Int? = null,

    @Json(name = "html_attributions")
    val htmlAttributions: List<String?>? = null,

    @Json(name = "height")
    val height: Int? = null
)

data class Place(

    @Json(name = "types")
    val types: List<String?>? = null,

    @Json(name = "business_status")
    val businessStatus: String? = null,

    @Json(name = "icon")
    val icon: String? = null,

    @Json(name = "rating")
    val rating: Any? = null,

    @Json(name = "icon_background_color")
    val iconBackgroundColor: String? = null,

    @Json(name = "reference")
    val reference: String? = null,

    @Json(name = "user_ratings_total")
    val userRatingsTotal: Int? = null,

    @Json(name = "scope")
    val scope: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "geometry")
    val geometry: Geometry? = null,

    @Json(name = "icon_mask_base_uri")
    val iconMaskBaseUri: String? = null,

    @Json(name = "vicinity")
    val vicinity: String? = null,

    @Json(name = "plus_code")
    val plusCode: PlusCode? = null,

    @Json(name = "place_id")
    val placeId: String? = null,

    @Json(name = "photos")
    val photos: List<PhotosItem?>? = null,

    @Json(name = "opening_hours")
    val openingHours: OpeningHours? = null
)

data class Location(

    @Json(name = "lng")
    val lng: Any? = null,

    @Json(name = "lat")
    val lat: Any? = null
)

data class OpeningHours(

    @Json(name = "open_now")
    val openNow: Boolean? = null
)

data class Northeast(

    @Json(name = "lng")
    val lng: Any? = null,

    @Json(name = "lat")
    val lat: Any? = null
)

data class Geometry(

    @Json(name = "viewport")
    val viewport: Viewport? = null,

    @Json(name = "location")
    val location: Location? = null
)

data class Southwest(

    @Json(name = "lng")
    val lng: Any? = null,

    @Json(name = "lat")
    val lat: Any? = null
)

data class PlusCode(

    @Json(name = "compound_code")
    val compoundCode: String? = null,

    @Json(name = "global_code")
    val globalCode: String? = null
)

data class Viewport(

    @Json(name = "southwest")
    val southwest: Southwest? = null,

    @Json(name = "northeast")
    val northeast: Northeast? = null
)
