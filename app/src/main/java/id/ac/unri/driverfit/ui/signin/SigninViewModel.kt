package id.ac.unri.driverfit.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.domain.usecase.SignInUseCase
import id.ac.unri.driverfit.domain.usecase.ValidateEmailUseCase
import id.ac.unri.driverfit.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val signInUseCase: SignInUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    val isLoogedIn = getUserUseCase.invoke().asLiveData()

    val _email = MutableStateFlow("")
    val email = _email.transform {
        emit(it to validateEmailUseCase(it))
    }.drop(1).flowOn(dispatcher).asLiveData()

    val _password = MutableStateFlow("")
    val password = _password.transform {
        emit(it to validatePasswordUseCase(it))
    }.drop(1).flowOn(dispatcher).asLiveData()

    private val _fulfilled = combine(
        _email,
        _password,
    ) { (email, password) ->
        email.isNotBlank()
                && validateEmailUseCase(email) == null
                && password.isNotBlank()
                && validatePasswordUseCase(password) == null
    }.flowOn(dispatcher)

    val fulfilled = _fulfilled.asLiveData()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asLiveData()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asLiveData()

    fun signIn() {
        viewModelScope.launch(dispatcher) {
            if (!_fulfilled.first()) return@launch
            _loading.value = true
            val email = _email.value
            val password = _password.value
            signInUseCase.invoke(email, password)
                .onFailure { e ->
                    Timber.e(e)
                    _snackbar.emit(e.message ?: "Failed to sign in. Try again later")
                }
            _loading.value = false
        }
    }

}