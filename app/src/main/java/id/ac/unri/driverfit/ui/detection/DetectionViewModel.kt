package id.ac.unri.driverfit.ui.detection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.model.Classification
import id.ac.unri.driverfit.domain.usecase.DetectFaceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val detectFaceUseCase: DetectFaceUseCase
) : ViewModel() {

    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asLiveData()

    private val _result = MutableStateFlow<Classification?>(null)
    val result = _result.asLiveData()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asLiveData()

    fun setResult(file: File) {
        _image.value = file

        viewModelScope.launch {
            _loading.value = true
            detectFaceUseCase(file)
                .onSuccess {
                    _result.value = it
                }.onFailure {
                    it.printStackTrace()
                }
            _loading.value = false
        }
    }

}