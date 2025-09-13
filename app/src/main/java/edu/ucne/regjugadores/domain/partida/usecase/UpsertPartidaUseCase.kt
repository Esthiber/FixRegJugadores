package edu.ucne.regjugadores.domain.partida.usecase

import edu.ucne.regjugadores.domain.partida.model.Partida
import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import java.util.Date
import javax.inject.Inject

class UpsertPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: Partida): Result<Int> {
        val j1Result = validateJugador1(partida.jugador1ID.toString())
        val j2Result = validateJugador2(partida.jugador2ID.toString())

        if (!j1Result.isValid)
            return Result.failure(IllegalArgumentException("Jugador 1: ${j1Result.error}}"))
        if (!j2Result.isValid)
            return Result.failure(IllegalArgumentException("Jugador 2: ${j2Result.error}}"))
        return runCatching { repository.upsert(partida) }
    }
}