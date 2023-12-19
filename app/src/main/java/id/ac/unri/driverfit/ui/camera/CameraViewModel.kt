package id.ac.unri.driverfit.ui.camera

import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ExecutionException

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val _cameraSelector = MutableLiveData<CameraSelector>().apply {
        value = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

    }
    val cameraSelector: LiveData<CameraSelector> = _cameraSelector

    private val _cameraProviderLiveData: MutableLiveData<ProcessCameraProvider> = MutableLiveData()
    val cameraProvider: LiveData<ProcessCameraProvider>
        get() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
            cameraProviderFuture.addListener(
                {
                    try {
                        _cameraProviderLiveData.setValue(cameraProviderFuture.get())
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                },
                ContextCompat.getMainExecutor(getApplication())
            )
            return _cameraProviderLiveData
        }

}