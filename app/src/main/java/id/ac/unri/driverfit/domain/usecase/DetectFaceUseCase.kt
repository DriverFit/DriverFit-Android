package id.ac.unri.driverfit.domain.usecase

import id.ac.unri.driverfit.domain.repository.DetectionRepository
import id.ac.unri.driverfit.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import java.io.File

class DetectFaceUseCase(
    private val userRepository: UserRepository,
    private val detectionRepository: DetectionRepository
) {

    suspend operator fun invoke(file: File) = runCatching {
        userRepository.getUser().firstOrNull() ?: throw Exception("Unauthorized")
        detectionRepository.detectExpression(file)
    }
}
