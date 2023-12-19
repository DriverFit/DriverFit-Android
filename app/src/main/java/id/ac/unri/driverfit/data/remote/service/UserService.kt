package id.ac.unri.driverfit.data.remote.service

import com.ch2ps215.mentorheal.data.remote.payload.Api
import id.ac.unri.driverfit.data.remote.payload.AuthGoogleRequest
import id.ac.unri.driverfit.data.remote.payload.EditUserRequest
import id.ac.unri.driverfit.data.remote.payload.SignInRequest
import id.ac.unri.driverfit.data.remote.payload.SignUpRequest
import id.ac.unri.driverfit.data.remote.payload.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {

    @POST("v1/accounts:signUp?key=AIzaSyAqTYYug-IVseFZT5cMy-k9TyrOiMVlhLk\n")
    suspend fun signUp(
        @Body req: SignUpRequest
    ): Response<UserResponse>

    @POST("v1/accounts:signInWithPassword?key=AIzaSyAqTYYug-IVseFZT5cMy-k9TyrOiMVlhLk\n")
    suspend fun signIn(
        @Body req: SignInRequest
    ): Response<UserResponse>

    @POST("api/user/auth-google")
    suspend fun authGoogle(
        @Body req: AuthGoogleRequest
    ): Response<Api<UserResponse>>

    @PUT("/api/user/edit")
    suspend fun edit(
        @Header("Authorization") token: String,
        @Body req: EditUserRequest
    ): Response<Api<UserResponse>>
}
