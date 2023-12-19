package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.repository.UserRepository

class SignOutUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() = runCatching {
        userRepository.signOut()
    }
}
