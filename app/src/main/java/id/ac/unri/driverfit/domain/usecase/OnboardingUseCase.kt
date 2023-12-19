package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class OnboardingUseCase(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return userRepository.onboarding()
    }

    suspend operator fun invoke(isAlreadyOnboarding: Boolean) {
        userRepository.onboarding(isAlreadyOnboarding)
    }
}
