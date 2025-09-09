package edu.ucne.regjugadores.domain.usecase

import edu.ucne.regjugadores.domain.model.Jugador
import edu.ucne.regjugadores.domain.repository.JugadorRepository
import javax.inject.Inject

class UpsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(jugador: Jugador): Result<Int> {
        val nombreResult = validateNombres(jugador.Nombres)
        val partidasResult = validatePartidas(jugador.Partidas.toString())

        if (!nombreResult.isValid) {
            return Result.failure(IllegalArgumentException(nombreResult.error))
        }
        if (!partidasResult.isValid) {
            return Result.failure(IllegalArgumentException(partidasResult.error))
        }
        return runCatching { repository.upsert(jugador) }
    }
}