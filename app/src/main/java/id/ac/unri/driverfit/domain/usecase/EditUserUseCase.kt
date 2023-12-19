package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.model.User
import id.ac.unri.driverfit.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class EditUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        password: String,
        photo: String? = null
    ): Result<User> = runCatching {
        val user = userRepository.getUser().firstOrNull() ?: throw RuntimeException("Unauthorized")
        userRepository.edit(user.token, name, password, photo)
    }
}
