package id.ac.unri.driverfit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.ac.unri.driverfit.R
import id.ac.unri.driverfit.domain.repository.UserRepository
import id.ac.unri.driverfit.domain.usecase.DarkThemeUseCase
import id.ac.unri.driverfit.domain.usecase.EditUserUseCase
import id.ac.unri.driverfit.domain.usecase.GetUserUseCase
import id.ac.unri.driverfit.domain.usecase.OnboardingUseCase
import id.ac.unri.driverfit.domain.usecase.SignInUseCase
import id.ac.unri.driverfit.domain.usecase.SignOutUseCase
import id.ac.unri.driverfit.domain.usecase.SignUpUseCase
import id.ac.unri.driverfit.domain.usecase.ValidateEmailUseCase
import id.ac.unri.driverfit.domain.usecase.ValidateNameUseCase
import id.ac.unri.driverfit.domain.usecase.ValidatePasswordUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideValidateNameUseCase(): ValidateNameUseCase {
        return ValidateNameUseCase(
            errorBlankMessage = R.string.error_name_is_required,
            errorMinMessage = R.string.error_name_min_char,
            errorMaxMessage = R.string.error_name_max_char
        )
    }

    @Provides
    @ViewModelScoped
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordUseCase(
            errorBlankMessage = R.string.error_password_is_required,
            errorMinMessage = R.string.error_password_min_char,
            errorMaxMessage = R.string.error_password_max_char,
            errorNotMatch = R.string.error_password_not_match
        )
    }

    @Provides
    @ViewModelScoped
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase(
            errorBlankEmail = R.string.error_email_is_required,
            errorIncorrectMessage = R.string.error_invalid_email
        )
    }

    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(userRepository: UserRepository): SignInUseCase {
        return SignInUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(userRepository: UserRepository): SignUpUseCase {
        return SignUpUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideEditUserUseCase(userRepository: UserRepository): EditUserUseCase {
        return EditUserUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignOutUseCase(userRepository: UserRepository): SignOutUseCase {
        return SignOutUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideOnboardingUseCase(userRepository: UserRepository): OnboardingUseCase {
        return OnboardingUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDarkThemeUseCase(userRepository: UserRepository): DarkThemeUseCase {
        return DarkThemeUseCase(userRepository)
    }
}
