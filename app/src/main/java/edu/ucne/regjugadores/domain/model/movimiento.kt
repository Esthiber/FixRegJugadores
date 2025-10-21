package edu.ucne.regjugadores.domain.model

data class movimiento(
    val partidaId: Int?,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)
