package id.ac.unri.driverfit.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val photo: String?,
    val token: String
)
