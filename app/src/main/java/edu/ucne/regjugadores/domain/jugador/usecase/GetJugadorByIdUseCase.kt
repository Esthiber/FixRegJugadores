package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import javax.inject.Inject

class GetJugadorByIdUseCase @Inject constructor(
    private val repository: JugadorRepository
){
    suspend operator fun invoke(id:Int?): Jugador? = repository.getJugadorById(id)
}