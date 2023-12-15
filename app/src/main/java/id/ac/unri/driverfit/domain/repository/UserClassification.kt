package id.ac.unri.driverfit.domain.repository

import android.graphics.Bitmap
import id.ac.unri.driverfit.domain.model.Classification
import java.io.File

interface UserClassification {
    fun classify(file: File): List<Classification>

    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}