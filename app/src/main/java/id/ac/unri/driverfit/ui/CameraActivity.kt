package id.ac.unri.driverfit.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.data.remote.TfLiteFaceUserClassifier
import id.ac.unri.driverfit.databinding.ActivityCameraBinding
import id.ac.unri.driverfit.ui.camera.CameraViewModel
import id.ac.unri.driverfit.ui.camera.UserImageAnalyzer
import id.ac.unri.driverfit.ui.utils.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class CameraActivity : ComponentActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private val viewModel: CameraViewModel by viewModels()

    private var isDetecting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        hideSystemBars()
        super.onCreate(savedInstanceState)

        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResult
        )

        binding = ActivityCameraBinding.inflate(layoutInflater)

        setContentView(binding.root)

        cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

        viewModel.cameraProvider.observe(this) { provider ->
            processCameraProvider = provider
            bindUseCase()
        }

        binding.galleryButton.setOnClickListener {
            val intentGallery = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }

            val chooser = Intent.createChooser(intentGallery, "Choose image")
            galleryLauncher.launch(chooser)
        }

        binding.cameraButton.setOnClickListener {
            isDetecting = !isDetecting
        }

        binding.switchButton.setOnClickListener {
            cameraSelector = when (cameraSelector) {
                CameraSelector.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
                else -> CameraSelector.DEFAULT_BACK_CAMERA
            }

            processCameraProvider.unbindAll()
            bindUseCase()
        }
    }

    private fun onGalleryResult(activityResult: ActivityResult) {
        try {
            if (activityResult.resultCode != Activity.RESULT_OK) {
                return
            }
            val selectedImg: Uri = activityResult.data?.data as Uri
            val file = selectedImg.uriToFile(this) // convert uri to file on detection fragment

            val intent = Intent().apply {
                BitmapUtils.reduceSize(file = file, rotate = false)
                putExtra(KEY_IMAGE_RESULT, selectedImg)
            }
            setResult(RESULT_SUCCESS, intent)
            finish()
        } catch (e: Exception) {
            Timber.e("Error onGalleryResult: ${e.message}")
        }
    }

    private fun bindUseCase() {
        cameraPreview = Preview.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        cameraPreview.setSurfaceProvider(binding.previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        val resolutionSelector = ResolutionSelector.Builder()
            .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)

        imageAnalysis = ImageAnalysis.Builder()
            .setResolutionSelector(resolutionSelector.build())
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        val cameraExecutor = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(cameraExecutor,
            UserImageAnalyzer(
                classifier = TfLiteFaceUserClassifier(
                    context = this@CameraActivity
                ),
                onResult = {
                    if (!isDetecting) return@UserImageAnalyzer
                    lifecycleScope.launch(Dispatchers.IO) {
                        val capturedImage =
                            imageCapture.takePicture(Executors.newSingleThreadExecutor())
                        val backCamera = cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA

                        val intent = Intent().apply {
                            BitmapUtils.reduceSize(capturedImage, backCamera)
                            putExtra(KEY_IMAGE_RESULT, capturedImage)
                        }

                        setResult(RESULT_SUCCESS, intent)
                        finish()
                    }
                }
            )
        )

        try {
            processCameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                cameraPreview,
                imageCapture,
                imageAnalysis
            )
        } catch (illegalStateException: IllegalStateException) {
            Timber.e(illegalStateException, "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Timber.e(illegalArgumentException, "IllegalArgumentException")
        }
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        insetsControllerCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun Uri.uriToFile(context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = context.createTempFile()

        val inputStream = contentResolver.openInputStream(this) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private suspend fun ImageCapture.takePicture(executor: Executor): File {
        val photoFile = runCatching {
            File.createTempFile("image", ".jpg").apply {
                Timber.i("")
            }
        }.getOrElse { e ->
            Timber.e(e, "Failed to create temporary file")
            File("/dev/null")
        }

        return suspendCancellableCoroutine { cont ->
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Timber.i("Image capture success")
                    cont.resume(photoFile)
                }

                override fun onError(ex: ImageCaptureException) {
                    Timber.e(ex, "Image capture failed")
                    cont.resumeWithException(ex)
                }
            })
        }
    }

    private fun Context.createTempFile(): File {
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("${System.currentTimeMillis()}", ".jpg", storageDir)
    }

    companion object {
        const val KEY_IMAGE_RESULT = "image_result"
        const val RESULT_SUCCESS = 1
    }
}
