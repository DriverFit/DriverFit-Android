package id.ac.unri.driverfit.ui.maps

import MapsPagingSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.GetRestPlaceUseCase
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.ui.utils.locationFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MapsViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    fusedLocationProviderClient: FusedLocationProviderClient,
    private val getRestPlaceUseCase: GetRestPlaceUseCase,
) : ViewModel() {
    val isLoogedIn = getUserUseCase.invoke()
        .filterNotNull()
        .asLiveData()

    private val locationFlow = fusedLocationProviderClient
        .locationFlow()
        .shareIn(viewModelScope, SharingStarted.Lazily)

    private val permissionGranted = MutableStateFlow(false)
    val places = permissionGranted
        .flatMapLatest {
            if (it) locationFlow else throw RuntimeException("Permission is needed")
        }.map {
            Pager(
                config = androidx.paging.PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    MapsPagingSource(
                        getRestPlaceUseCase = getRestPlaceUseCase,
                        lat = it.latitude,
                        lng = it.longitude,
                    )
                }
            ).flow
        }

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asLiveData()

    fun loadPlacesIfPermissionGranted(granted: Boolean) {
        permissionGranted.value = granted
    }

    companion object {
        const val KEY_PLACE_ID = "place_id"
    }
}