package id.ac.unri.driverfit.domain.repository

import android.graphics.Bitmap
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.ml.SleepyDetection
import java.io.File

interface UserClassification {
    fun classify(file: File) : SleepyDetection.Outputs?

    fun classify(bitmap: Bitmap, rotation: Int): Classification
}