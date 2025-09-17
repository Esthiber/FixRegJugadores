package edu.ucne.regjugadores.domain.partida.model

import java.util.Date

data class Partida(
    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1ID: Int = 0,
    val jugador2ID: Int = 0,
    val ganadorID: Int? = null,
    val esFinalizada: Boolean = false,
)
