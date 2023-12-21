package id.ac.unri.driverfit.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.DarkThemeUseCase
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.domain.usecase.SignOutUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    val dispatcher: CoroutineDispatcher,
    private val signOutUseCase: SignOutUseCase,
    private val darkThemeUseCase: DarkThemeUseCase
) : ViewModel() {
    val user = getUserUseCase().asLiveData()
    val darkTheme = darkThemeUseCase().asLiveData()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asLiveData()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asSharedFlow()

    fun signOut() {
        viewModelScope.launch {
            _loading.value = true
            signOutUseCase().onFailure { e ->
                Timber.e(e)
                _snackbar.emit("Sign out was failed")
            }
            _loading.value = false
        }
    }

    fun darkTheme(isDarkTheme: Boolean?) {
        viewModelScope.launch(dispatcher) {
            darkThemeUseCase(isDarkTheme)
        }
    }

}