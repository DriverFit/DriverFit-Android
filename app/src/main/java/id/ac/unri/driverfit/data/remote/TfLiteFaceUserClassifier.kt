package id.ac.unri.driverfit.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Surface
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.domain.repository.UserClassification
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File

class TfLiteFaceUserClassifier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResult: Int = 3
) : UserClassification {

    private var classifier: ImageClassifier? = null

    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResult)
            .setScoreThreshold(threshold)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "model.tflite",
                options
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun classify(file: File): List<Classification> {
        if (classifier == null) {
            setupClassifier()
        }

        val bmp = BitmapFactory.decodeFile(file.path)

        val imageprocessor = ImageProcessor.Builder().build()
        val tensorImage = imageprocessor.process(TensorImage.fromBitmap(bmp))

        val result = classifier?.classify(tensorImage)

        return result?.flatMap { classification ->
            classification.categories.map { category ->
                Classification(
                    label = category.label,
                    scores = category.score.toInt()
                )
            }
        }?.distinctBy { it.label } ?: emptyList()
    }

    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier == null) {
            setupClassifier()
        }

        val imageprocessor = ImageProcessor.Builder().build()
        val tensorImage = imageprocessor.process(TensorImage.fromBitmap(bitmap))


        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()

        val result = classifier?.classify(tensorImage, imageProcessingOptions)

        return result?.flatMap { classification ->
            classification.categories.map { category ->
                Classification(
                    label = category.label,
                    scores = category.score.toInt()
                )
            }
        }?.distinctBy { it.label } ?: emptyList()
    }


    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when (rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }

}