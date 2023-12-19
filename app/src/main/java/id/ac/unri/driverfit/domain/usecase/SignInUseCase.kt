package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.model.User
import id.ac.unri.driverfit.domain.repository.UserRepository

class SignInUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): Result<User> = runCatching {
        userRepository.signIn(email, password)
    }
}
