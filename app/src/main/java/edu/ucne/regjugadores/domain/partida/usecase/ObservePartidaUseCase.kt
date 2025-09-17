package edu.ucne.regjugadores.domain.partida.usecase

import edu.ucne.regjugadores.domain.partida.model.Partida
import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    operator fun invoke(): Flow<List<Partida>> = repository.observePartida()
}