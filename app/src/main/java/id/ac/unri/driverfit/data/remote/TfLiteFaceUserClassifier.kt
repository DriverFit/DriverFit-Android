package id.ac.unri.driverfit.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Surface
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.domain.repository.UserClassification
import id.ac.unri.driverfit.ml.SleepyDetection
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TfLiteFaceUserClassifier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResult: Int = 3
) : UserClassification {

    private var classifier: SleepyDetection? = null

    private fun setupModel() {
        val model = SleepyDetection.newInstance(context)

        classifier = model
    }

    override fun classify(file: File): Classification {
        if (classifier == null) {
            setupModel()
        }

        val bmp = BitmapFactory.decodeFile(file.path)

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)

        // set the byte order of the buffer to be in big-endian format
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        bmp.getPixels(intValues, 0, bmp.width, 0, 0, bmp.width, bmp.height)

        //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        var pixel = 0
        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 255))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = classifier?.process(inputFeature0)

        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

        val confidences = outputFeature0!!.floatArray

        return if (confidences[0] > 0.5F) {
            Classification("Fatigue", confidences[0])
        } else {
            Classification("Active", confidences[0])
        }
    }

    override fun classify(bitmap: Bitmap, rotation: Int): Classification {
        if (classifier == null) {
            setupModel()
        }

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)

        // set the byte order of the buffer to be in big-endian format
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        var pixel = 0
        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 255))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = classifier?.process(inputFeature0)

        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

        val confidences = outputFeature0!!.floatArray

        return if (confidences[0] > 0.5F) {
            Classification("Fatigue", confidences[0])
        } else {
            Classification("Active", confidences[0])
        }
    }


    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when (rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }

    fun fileToByteBuffer(file: File): ByteBuffer {
        val buffer = ByteBuffer.allocate(256 * 256 * 3)

        try {
            val fileChannel = RandomAccessFile(file, "r").channel
            fileChannel.read(buffer)
            fileChannel.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        buffer.flip()
        return buffer
    }

    fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {

        val byteBuffer: ByteBuffer = ByteBuffer.allocate(256 * 256 * 3)

        bitmap.copyPixelsToBuffer(byteBuffer)

        byteBuffer.rewind()

        return byteBuffer
    }

    fun bitmapToByteBufferTemp(bitmap: Bitmap): ByteBuffer {

        // Calculate the number of bytes required to store the bitmap data
        val byteCount = bitmap.byteCount

        // Create a ByteBuffer with the calculated capacity
        val buffer = ByteBuffer.allocateDirect(byteCount)

        // Copy the bitmap data into the ByteBuffer
        bitmap.copyPixelsToBuffer(buffer)

        // Rewind the buffer to the beginning before use
        buffer.rewind()

        return buffer
    }

}