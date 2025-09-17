package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    operator fun invoke(): Flow<List<Jugador>> = repository.observeJugador()
}