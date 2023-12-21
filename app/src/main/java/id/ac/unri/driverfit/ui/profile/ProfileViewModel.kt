package id.ac.unri.driverfit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
) : ViewModel() {

    val isLoogedIn = getUserUseCase.invoke()
        .filterNotNull()
        .asLiveData()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asLiveData()

}