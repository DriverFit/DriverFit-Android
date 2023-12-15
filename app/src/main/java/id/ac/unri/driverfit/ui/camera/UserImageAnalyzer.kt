package id.ac.unri.driverfit.ui.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.domain.repository.UserClassification

class UserImageAnalyzer(
    private val classifier: UserClassification,
    private val onResult: (List<Classification>) -> Unit,
) : ImageAnalysis.Analyzer {

    private var framecounter = 0
    override fun analyze(image: ImageProxy) {
        if (framecounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(224, 224)

            val result = classifier.classify(bitmap, rotationDegrees)
            onResult(result)
        }

        framecounter++

        image.close()
    }
}