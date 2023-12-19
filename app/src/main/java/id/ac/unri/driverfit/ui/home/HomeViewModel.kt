package id.ac.unri.driverfit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase
) : ViewModel() {

    val user = getUserUseCase().asLiveData()
}