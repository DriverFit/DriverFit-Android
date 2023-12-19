package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.model.User
import id.ac.unri.driverfit.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}
