package id.ac.unri.driverfit.domain.repository

import id.ac.unri.driverfit.domain.model.Classification
import java.io.File

interface DetectionRepository {

    suspend fun detectExpression(file: File): Classification

}
