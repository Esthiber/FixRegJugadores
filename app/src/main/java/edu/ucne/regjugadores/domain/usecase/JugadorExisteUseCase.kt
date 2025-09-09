package edu.ucne.regjugadores.domain.usecase

import edu.ucne.regjugadores.domain.repository.JugadorRepository
import javax.inject.Inject

class JugadorExisteUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(nombre: String, idJugadorActual: Int?):Boolean {
        return repository.jugadorExisteByNombre(nombre, idJugadorActual)
    }
}