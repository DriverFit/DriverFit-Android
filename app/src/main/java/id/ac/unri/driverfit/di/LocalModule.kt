package id.ac.unri.driverfit.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.ac.unri.driverfit.data.local.UserLocalDataSource
import id.ac.unri.driverfit.data.local.datastore.UserPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private val Context.dataStore by preferencesDataStore(name = "app_preferences")

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext
        context: Context
    ): UserPreferences {
        return UserPreferences(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(userPreferences: UserPreferences): UserLocalDataSource {
        return UserLocalDataSource(userPreferences)
    }
}
