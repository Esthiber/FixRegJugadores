package edu.ucne.regjugadores.data.remote

import edu.ucne.regjugadores.data.remote.dto.movimientosDto
import edu.ucne.regjugadores.data.remote.dto.request.jugadores.JugadorRequest
import edu.ucne.regjugadores.data.remote.dto.response.jugadores.JugadorResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: TicTacToeApi
) {
    suspend fun getMovimientos(partidaId: Int): List<movimientosDto> =
        api.getMovimientos(partidaId)

    suspend fun postMovimiento(movimiento: movimientosDto): Unit =
        api.postMovimiento(movimiento)


    suspend fun createJugador(req: JugadorRequest): Resource<JugadorResponse> {
        return try {
            val response = api.createJugador(req)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacia del servidor")
            } else {
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrio un error desconocido"}")
        }
    }

    suspend fun updateJugador(id: Int, req: JugadorRequest): Resource<Unit> {
        return try {
            val response = api.updateJugador(id, req)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrio un error desconocido"}")
        }
    }

    suspend fun  deleteJugador(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteJugador(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrio un error desconocido"}")
        }
    }
}