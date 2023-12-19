package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.model.User
import id.ac.unri.driverfit.domain.repository.UserRepository

class SignUpUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<User> = runCatching {
        userRepository.signUp(name, email, password)
    }
}
