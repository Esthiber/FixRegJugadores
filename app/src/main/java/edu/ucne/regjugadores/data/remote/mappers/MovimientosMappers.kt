package edu.ucne.regjugadores.data.remote.mappers

import edu.ucne.regjugadores.data.remote.dto.movimientosDto
import edu.ucne.regjugadores.domain.model.movimiento

fun movimientosDto.toDomain(): movimiento {
    return movimiento(
        partidaId = this.partidaId,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}

fun movimiento.toDto(): movimientosDto {
    return movimientosDto(
        partidaId = this.partidaId,
        jugador = this.jugador,
        posicionFila = this.posicionFila,
        posicionColumna = this.posicionColumna
    )
}