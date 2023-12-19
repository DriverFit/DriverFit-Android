package id.ac.unri.driverfit.domain.usecase

import androidx.annotation.StringRes

class ValidatePasswordUseCase(
    @StringRes private val errorBlankMessage: Int,
    @StringRes private val errorMinMessage: Int,
    @StringRes private val errorMaxMessage: Int,
    @StringRes private val errorNotMatch: Int
) {

    operator fun invoke(password: String): Int? {
        if (password.isBlank()) return errorBlankMessage

        if (password.length < 8) return errorMinMessage

        if (password.length > 50) return errorMaxMessage

        return null
    }

    operator fun invoke(password: String, confirm: String): Int? {
        if (password != confirm) return errorNotMatch
        return null
    }
}
