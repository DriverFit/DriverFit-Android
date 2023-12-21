package id.ac.unri.driverfit.data

import id.ac.unri.driverfit.data.remote.TfLiteFaceUserClassifier
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.domain.repository.DetectionRepository
import java.io.File

class DefaultDetectionRepository(
    private val tfLiteUserClassifierDataSource: TfLiteFaceUserClassifier
) : DetectionRepository {
    override suspend fun detectExpression(file: File): Classification {
        return tfLiteUserClassifierDataSource.classify(file)
    }
}
