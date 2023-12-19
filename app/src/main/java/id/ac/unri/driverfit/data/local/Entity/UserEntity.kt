package id.ac.unri.driverfit.data.local.Entity

data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val photo: String?,
    val token: String
)
