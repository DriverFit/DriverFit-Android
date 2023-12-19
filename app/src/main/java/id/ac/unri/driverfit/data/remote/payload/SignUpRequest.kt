package id.ac.unri.driverfit.data.remote.payload

import com.squareup.moshi.Json

data class SignUpRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)
