package edu.ucne.regjugadores.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.regjugadores.data.remote.TicTacToeApi
import edu.ucne.regjugadores.data.remote.repository.MovimientosRepositoryImpl
import edu.ucne.regjugadores.domain.repository.MovimientosRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net"

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesTicTacToeApi(moshi: Moshi, okHttpClient: OkHttpClient): TicTacToeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(TicTacToeApi::class.java)
    }

}

@InstallIn(SingletonComponent::class)
@Module
abstract class  RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovimientosRepository(
        movimientosRepositoryImpl: MovimientosRepositoryImpl
    ): MovimientosRepository
}

