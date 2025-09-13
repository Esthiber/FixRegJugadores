package edu.ucne.regjugadores.domain.partida.usecase

import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import javax.inject.Inject

class DeletePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
){
    suspend operator  fun invoke(id:Int) = repository.deletePartidaById(id)
}