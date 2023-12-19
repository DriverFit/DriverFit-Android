package id.ac.unri.driverfit.ui.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.R
import id.ac.unri.driverfit.domain.model.Onboarding
import id.ac.unri.driverfit.domain.usecase.DarkThemeUseCase
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.domain.usecase.OnboardingUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase,
    private val dispatcher: CoroutineDispatcher,
    getUserUseCase: GetUserUseCase,
    darkThemeUseCase: DarkThemeUseCase
) : ViewModel() {
    val onboarding = onboardingUseCase.invoke()
        .flowOn(dispatcher)
    val loggedIn = getUserUseCase()
        .map { it != null }
        .flowOn(dispatcher)
    val darkTheme = darkThemeUseCase()
        .flowOn(dispatcher)

    val data = MutableLiveData<List<Onboarding>>().apply {
        value = setupList()
    }

    private fun setupList(): List<Onboarding> {
        // title, desk ,image
        return listOf(
            Onboarding(
                "Selamat Datang di Driver Fit",
                "Driver Fit adalah aplikasi yang dapat membantu anda untuk mencari tempat istirahat terdekat",
                R.mipmap.ic_launcher_driverfit
            ),
            Onboarding(
                "Mendeteksi Wajah Anda",
                "Mendeteksi wajah anda untuk mengetahui apakah anda mengantuk atau tidak",
                R.mipmap.ic_launcher_driverfit
            ),
        )
    }

    fun onboarding(isAlreadyOnboarding: Boolean) {
        viewModelScope.launch(dispatcher) {
            onboardingUseCase(isAlreadyOnboarding)
        }
    }
}
