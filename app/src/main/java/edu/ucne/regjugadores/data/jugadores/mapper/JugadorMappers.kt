package edu.ucne.regjugadores.data.jugadores.mapper

import edu.ucne.regjugadores.data.jugadores.local.JugadorEntity
import edu.ucne.regjugadores.domain.jugador.model.Jugador

fun JugadorEntity.toDomain(): Jugador = Jugador(
    id = id,
    nombres = nombres,
    email = email,
    isPendingCreate = isPendingCreate
)

fun Jugador.toEntity(): JugadorEntity = JugadorEntity(
    id = id,
    nombres = nombres,
    email = email,
    isPendingCreate = isPendingCreate
)