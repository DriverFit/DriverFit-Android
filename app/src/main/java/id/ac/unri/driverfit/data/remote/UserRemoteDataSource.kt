package id.ac.unri.driverfit.data.remote

import id.ac.unri.driverfit.data.remote.payload.AuthGoogleRequest
import id.ac.unri.driverfit.data.remote.payload.EditUserRequest
import id.ac.unri.driverfit.data.remote.payload.SignInRequest
import id.ac.unri.driverfit.data.remote.payload.SignUpRequest
import id.ac.unri.driverfit.data.remote.payload.UserResponse
import id.ac.unri.driverfit.data.remote.service.UserService
import okhttp3.ResponseBody
import org.json.JSONObject

class UserRemoteDataSource(
    private val userService: UserService
) {

    suspend fun signUp(req: SignUpRequest): UserResponse {
        val res = userService.signUp(req)
        val data = res.takeIf { it.isSuccessful }?.body()
        if (data == null) {
            res.errorBody()?.let { throw RuntimeException(it.getErrorMessage()) }
        }

        return data ?: throw RuntimeException("Response body is empty")
    }

    suspend fun signIn(req: SignInRequest): UserResponse {
        val res = userService.signIn(req)
        val data = res.takeIf { it.isSuccessful }?.body()
        if (data == null) {
            res.errorBody()?.let { throw RuntimeException(it.getErrorMessage()) }
        }

        return data ?: throw RuntimeException("Response body is empty")
    }

    suspend fun authGoogle(req: AuthGoogleRequest): UserResponse {
        TODO()
    }

    suspend fun edit(token: String, req: EditUserRequest): UserResponse {
        val res = userService.edit("Bearer $token", req)
        val data = res.takeIf { it.isSuccessful }?.body()?.data
        if (data == null) {
            res.errorBody()?.let { throw RuntimeException(it.getErrorMessage()) }
        }

        return data ?: throw RuntimeException("Response body is empty")
    }
}

fun ResponseBody.getErrorMessage(key: String = "error"): String {
    return JSONObject(charStream().readText()).getJSONObject(key).getString("message")
}
