package id.ac.unri.driverfit.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.domain.usecase.SignUpUseCase
import id.ac.unri.driverfit.domain.usecase.ValidateEmailUseCase
import id.ac.unri.driverfit.domain.usecase.ValidateNameUseCase
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
class SignUpViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    val isLoogedIn = getUserUseCase.invoke().asLiveData()

    val _name = MutableStateFlow("")
    val name = _name.transform {
        emit(it to validateNameUseCase(it))
    }.drop(1).flowOn(dispatcher).asLiveData()

    val _email = MutableStateFlow("")
    val email = _email.transform {
        emit(it to validateEmailUseCase(it))
    }.drop(1).flowOn(dispatcher).asLiveData()

    val _password = MutableStateFlow("")
    val password = _password.transform {
        emit(it to validatePasswordUseCase(it))
    }.drop(1).flowOn(dispatcher).asLiveData()

    val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.transform {
        emit(it to validatePasswordUseCase(_password.value, it))
    }.drop(1).flowOn(dispatcher).asLiveData()


    private val _fulfilled = combine(
        _name,
        _email,
        _password,
        _confirmPassword
    ) { (name, email, password, confirmPassword) ->
        name.isNotBlank()
                && validateNameUseCase(name) == null
                && email.isNotBlank()
                && validateEmailUseCase(email) == null
                && password.isNotBlank()
                && validatePasswordUseCase(password) == null
                && confirmPassword.isNotBlank()
                && validatePasswordUseCase(password, confirmPassword) == null
    }.flowOn(dispatcher)

    val fulfilled = _fulfilled.asLiveData()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asLiveData()

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asLiveData()

    fun signUp() {
        viewModelScope.launch(dispatcher) {
            if (!_fulfilled.first()) return@launch
            _loading.value = true
            val name = this@SignUpViewModel._name.value
            val email = _email.value
            val password = _password.value
            signUpUseCase.invoke(name, email, password)
                .onFailure { e ->
                    Timber.e(e)
                    _snackbar.emit(e.message ?: "Failed to sign in. Try again later")
                }
            _loading.value = false
        }
    }
}