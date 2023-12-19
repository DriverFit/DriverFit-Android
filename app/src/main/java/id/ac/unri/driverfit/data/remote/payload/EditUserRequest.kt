package id.ac.unri.driverfit.data.remote.payload

data class EditUserRequest(
    val name: String,
    val password: String,
    val photo: String?
)
