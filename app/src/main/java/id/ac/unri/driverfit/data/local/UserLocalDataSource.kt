package id.ac.unri.driverfit.data.local

import id.ac.unri.driverfit.data.local.datastore.UserPreferences
import id.ac.unri.driverfit.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserLocalDataSource(
    private val userPreferences: UserPreferences
) {

    suspend fun save(user: UserEntity) {
        userPreferences.saveUser(user)
    }

    fun getUser(): Flow<UserEntity?> {
        return userPreferences.getUser()
    }

    suspend fun delete() {
        userPreferences.deleteUser()
    }

    suspend fun onboarding(isAlreadyOnboarding: Boolean) {
        userPreferences.onboarding(isAlreadyOnboarding)
    }

    fun onboarding(): Flow<Boolean> {
        return userPreferences.onboarding()
    }

    suspend fun darkTheme(isDarkTheme: Boolean?) {
        userPreferences.darkTheme(isDarkTheme)
    }

    fun darkTheme(): Flow<Boolean?> {
        return userPreferences.darkTheme()
    }
}
