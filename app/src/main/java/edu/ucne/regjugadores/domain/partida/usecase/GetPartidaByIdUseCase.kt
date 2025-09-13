package edu.ucne.regjugadores.domain.partida.usecase

import edu.ucne.regjugadores.domain.partida.model.Partida
import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import javax.inject.Inject

class GetPartidaByIdUseCase @Inject constructor(
    private val repository: PartidaRepository
){
    suspend operator fun invoke(id:Int?): Partida? = repository.getPartidaById(id)
}