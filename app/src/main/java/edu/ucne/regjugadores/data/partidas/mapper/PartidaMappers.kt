package edu.ucne.regjugadores.data.partidas.mapper

import edu.ucne.regjugadores.data.partidas.local.PartidaEntity
import edu.ucne.regjugadores.domain.partida.model.Partida

fun PartidaEntity.toDomain(): Partida = Partida(
    partidaId = partidaId,
    fecha = fecha,
    jugador1ID = jugador1ID,
    jugador2ID = jugador2ID,
    ganadorID = ganadorID,
    esFinalizada = esFinalizada
)

fun Partida.toEntity(): PartidaEntity = PartidaEntity(
    partidaId = partidaId,
    fecha = fecha,
    jugador1ID = jugador1ID,
    jugador2ID = jugador2ID,
    ganadorID = ganadorID,
    esFinalizada = esFinalizada
)
