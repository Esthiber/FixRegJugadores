package edu.ucne.regjugadores.domain.usecase

import edu.ucne.regjugadores.domain.repository.JugadorRepository
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(id:Int) = repository.deleteJugadorbyId(id)
}