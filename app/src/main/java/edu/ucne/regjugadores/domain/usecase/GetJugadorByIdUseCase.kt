package edu.ucne.regjugadores.domain.usecase

import edu.ucne.regjugadores.domain.model.Jugador
import edu.ucne.regjugadores.domain.repository.JugadorRepository
import javax.inject.Inject

class GetJugadorByIdUseCase @Inject constructor(
    private val repository: JugadorRepository
){
    suspend operator fun invoke(id:Int?): Jugador? = repository.getJugadorById(id)
}