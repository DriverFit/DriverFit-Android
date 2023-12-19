package id.ac.unri.driverfit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.unri.driverfit.data.DefaultUserRepository
import id.ac.unri.driverfit.data.local.UserLocalDataSource
import id.ac.unri.driverfit.data.remote.UserRemoteDataSource
import id.ac.unri.driverfit.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userLocalDataSource: UserLocalDataSource,
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository {
        return DefaultUserRepository(userLocalDataSource, userRemoteDataSource)
    }
}
