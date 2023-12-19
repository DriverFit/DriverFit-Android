package id.ac.unri.driverfit.data

import id.ac.unri.driverfit.data.local.UserLocalDataSource
import id.ac.unri.driverfit.data.mapper.asEntity
import id.ac.unri.driverfit.data.mapper.asModel
import id.ac.unri.driverfit.data.remote.UserRemoteDataSource
import id.ac.unri.driverfit.data.remote.payload.EditUserRequest
import id.ac.unri.driverfit.data.remote.payload.SignInRequest
import id.ac.unri.driverfit.data.remote.payload.SignUpRequest
import id.ac.unri.driverfit.domain.model.User
import id.ac.unri.driverfit.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultUserRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun signUp(name: String, email: String, password: String): User {
        val req = SignUpRequest(
            name = name,
            email = email,
            password = password
        )
        val res = userRemoteDataSource.signUp(req).asEntity()
        userLocalDataSource.save(res)
        return res.asModel()
    }

    override suspend fun signIn(email: String, password: String): User {
        val req = SignInRequest(
            email = email,
            password = password
        )
        val res = userRemoteDataSource.signIn(req).asEntity()
        userLocalDataSource.save(res)
        return res.asModel()
    }

    override suspend fun authGoogle(idToken: String): User {
        TODO()
    }

    override suspend fun signOut() {
        userLocalDataSource.delete()
    }

    override fun getUser(): Flow<User?> {
        return userLocalDataSource.getUser().map { it?.asModel() }
    }

    override suspend fun edit(token: String, name: String, password: String, photo: String?): User {
        val req = EditUserRequest(
            name = name,
            password = password,
            photo = photo
        )
        val res = userRemoteDataSource.edit(token, req).asEntity()
        userLocalDataSource.save(res)
        return res.asModel()
    }

    override suspend fun onboarding(isAlreadyOnboarding: Boolean) {
        userLocalDataSource.onboarding(isAlreadyOnboarding)
    }

    override fun onboarding(): Flow<Boolean> {
        return userLocalDataSource.onboarding()
    }

    override suspend fun darkTheme(isDarkTheme: Boolean?) {
        userLocalDataSource.darkTheme(isDarkTheme)
    }

    override fun darkTheme(): Flow<Boolean?> {
        return userLocalDataSource.darkTheme()
    }
}
