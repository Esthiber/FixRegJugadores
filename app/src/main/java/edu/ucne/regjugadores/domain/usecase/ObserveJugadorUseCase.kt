package edu.ucne.regjugadores.domain.usecase

import edu.ucne.regjugadores.domain.model.Jugador
import edu.ucne.regjugadores.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    operator fun invoke(): Flow<List<Jugador>> = repository.observeJugador()
}