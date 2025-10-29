package edu.ucne.regjugadores.data.remote.dto.response.jugadores

import com.squareup.moshi.Json

data class JugadorResponse(
    @Json(name = "jugadorId") val jugadorId: Int?,
    val nombres: String,
    val email: String
)
