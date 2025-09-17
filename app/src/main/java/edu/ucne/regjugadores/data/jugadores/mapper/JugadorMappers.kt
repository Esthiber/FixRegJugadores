package edu.ucne.regjugadores.data.jugadores.mapper

import edu.ucne.regjugadores.data.jugadores.local.JugadorEntity
import edu.ucne.regjugadores.domain.jugador.model.Jugador

fun JugadorEntity.toDomain(): Jugador = Jugador(
    JugadorId = jugadorId,
    Nombres = nombres,
    Partidas = partidas
)

fun Jugador.toEntity(): JugadorEntity = JugadorEntity(
    jugadorId = JugadorId,
    nombres = Nombres,
    partidas = Partidas
)