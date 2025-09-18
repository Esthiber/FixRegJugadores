package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import javax.inject.Inject

class CanDeleteJugadorUseCase @Inject constructor(
    private val partidaRepository: PartidaRepository
) {
    suspend operator fun invoke(jugadorId: Int): Boolean {
        return !partidaRepository.hasPartidas(jugadorId)
    }
}
