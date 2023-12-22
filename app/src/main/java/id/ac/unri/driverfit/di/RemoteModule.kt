package id.ac.unri.driverfit.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.ac.unri.driverfit.BuildConfig
import id.ac.unri.driverfit.data.remote.GoogleMapRemoteDataSource
import id.ac.unri.driverfit.data.remote.TfLiteFaceUserClassifier
import id.ac.unri.driverfit.data.remote.UserRemoteDataSource
import id.ac.unri.driverfit.data.remote.service.GoogleMapService
import id.ac.unri.driverfit.data.remote.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
        }

        val callFactory = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://identitytoolkit.googleapis.com/")
            .callFactory(callFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(retrofit: Retrofit): UserRemoteDataSource {
        val userService = retrofit.create<UserService>()
        return UserRemoteDataSource(userService)
    }

    @Provides
    @Singleton
    fun provideTfLiteUserClassifierDataSource(
        @ApplicationContext context: Context
    ): TfLiteFaceUserClassifier {
        return TfLiteFaceUserClassifier(context)
    }

    @Provides
    @Singleton
    fun provideGoogleMapRemoteDataSource(
        @ApplicationContext context: Context,
        retrofit: Retrofit
    ): GoogleMapRemoteDataSource {
        val googleMapService = retrofit.create<GoogleMapService>()

        Places.initialize(context, "AIzaSyBfRJ-jw_2gIFzyGU_NJbWvKQGZSSME3_I")
        val placesClient = Places.createClient(context)

        return GoogleMapRemoteDataSource(googleMapService)
    }
}
