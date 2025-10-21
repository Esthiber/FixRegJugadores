package edu.ucne.regjugadores.data.remote

import edu.ucne.regjugadores.data.remote.dto.movimientosDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: TicTacToeApi
) {
    suspend fun getMovimientos(partidaId: Int): List<movimientosDto> =
        api.getMovimientos(partidaId)

    suspend fun postMovimiento(movimiento: movimientosDto): Unit =
        api.postMovimiento(movimiento)
}