package id.ac.unri.driverfit.ui.camera

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.domain.repository.UserClassification

class UserImageAnalyzer(
    private val classifier: UserClassification,
    private val onResult: (Classification) -> Unit,
) : ImageAnalysis.Analyzer {

    private var framecounter = 0
    override fun analyze(image: ImageProxy) {

        if (framecounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            var bitmap = image.toBitmap()

            bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false)

            val result = classifier.classify(bitmap, rotationDegrees)
            onResult(result)
        }

        framecounter++

        image.close()
    }
}