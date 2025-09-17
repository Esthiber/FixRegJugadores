package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import javax.inject.Inject

class JugadorExisteUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(nombre: String, idJugadorActual: Int?):Boolean {
        return repository.jugadorExisteByNombre(nombre, idJugadorActual)
    }
}