package id.ac.unri.driverfit.ui.maps

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.model.Place
import id.ac.unri.driverfit.domain.usecase.GetPlaceUseCase
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.ui.utils.locationFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    fusedLocationProviderClient: FusedLocationProviderClient,
    private val getPlaceUseCase: GetPlaceUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val locationFlow = fusedLocationProviderClient
        .locationFlow()
        .shareIn(viewModelScope, SharingStarted.Lazily)

    private val _place = MutableStateFlow<Place?>(null)
    val place = _place.asStateFlow()

    val isLoogedIn = getUserUseCase.invoke()
        .filterNotNull()
        .asLiveData()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asLiveData()

    init {
        loadPlace()
    }

    private fun loadPlace() {
        viewModelScope.launch(dispatcher) {
            val placeId = savedStateHandle.get<String>(KEY_PLACE_ID) ?: return@launch
            getPlaceUseCase(placeId)
                .onSuccess { place ->
                    _place.value = place
                }
                .onFailure { e ->
                    Timber.e(e)
                    _snackbar.emit("Failed to load place")
                }
        }
    }

    companion object {
        const val KEY_PLACE_ID = "place_id"
    }
}