package edu.ucne.regjugadores.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.regjugadores.data.logros.repository.LogroRepositoryImpl
import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LogroRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLogroRepository(
        logroRepositoryImpl: LogroRepositoryImpl
    ): LogroRepository
}