package edu.ucne.regjugadores.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.regjugadores.data.jugadores.repository.JugadorRepositoryImpl
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class JugadorRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindJugadorRepository(
        jugadorRepositoryImpl: JugadorRepositoryImpl
    ): JugadorRepository
}