package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
) {
    suspend operator fun invoke(id: String): Resource<Unit> = repository.deleteJugadorById(id)

    suspend operator fun invoke(jugador: Jugador): Resource<Unit> = repository.deleteJugador(jugador)
}