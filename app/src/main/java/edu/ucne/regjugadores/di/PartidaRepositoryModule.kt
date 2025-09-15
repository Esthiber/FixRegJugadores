package edu.ucne.regjugadores.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.regjugadores.data.partidas.repository.PartidaRepositoryImpl
import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PartidaRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPartidaRepository(
        partidaRepositoryImpl: PartidaRepositoryImpl
    ): PartidaRepository
}