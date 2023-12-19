package id.ac.unri.driverfit.data.remote.payload

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "localId")
    val id: String,

    @Json(name = "displayName")
    val name: String? = null,

    @Json(name = "email")
    val email: String,

    @Json(name = "idToken")
    val token: String,

    @Json(name = "expiresIn")
    val expiresIn: String? = null,

    @Json(name = "kind")
    val kind: String? = null,

    @Json(name = "registered")
    val registered: Boolean? = null,

    @Json(name = "refreshToken")
    val refreshToken: String? = null,

    @Json(name = "error")
    val error: Error? = null
)

data class ErrorsItem(

    @Json(name = "reason")
    val reason: String? = null,

    @Json(name = "domain")
    val domain: String? = null,

    @Json(name = "message")
    val message: String? = null
)

data class Error(

    @Json(name = "code")
    val code: Int? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "errors")
    val errors: List<ErrorsItem?>? = null
)
