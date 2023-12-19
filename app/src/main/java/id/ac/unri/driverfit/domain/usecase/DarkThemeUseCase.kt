package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class DarkThemeUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(isDarkTheme: Boolean?) {
        userRepository.darkTheme(isDarkTheme)
    }

    operator fun invoke(): Flow<Boolean?> {
        return userRepository.darkTheme()
    }
}
