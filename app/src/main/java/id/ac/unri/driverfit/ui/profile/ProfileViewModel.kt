package id.ac.unri.driverfit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val isLoogedIn = getUserUseCase.invoke()
        .filterNotNull()
        .asLiveData()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asLiveData()
    fun signout(): Boolean {
        var success = false
        viewModelScope.launch {
            signOutUseCase()
                .onSuccess {
                    success = true
                }
                .onFailure { e ->
                    Timber.e(e)
                    _snackbar.emit("Sign out was failed")
                }
        }
        return success
    }

}