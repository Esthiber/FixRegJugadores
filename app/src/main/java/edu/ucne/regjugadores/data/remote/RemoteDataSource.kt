package edu.ucne.regjugadores.data.remote

import android.util.Log
import edu.ucne.regjugadores.data.remote.dto.movimientosDto
import edu.ucne.regjugadores.data.remote.dto.request.jugadores.JugadorRequest
import edu.ucne.regjugadores.data.remote.dto.response.jugadores.JugadorResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: TicTacToeApi
) {
    suspend fun getMovimientos(partidaId: Int): List<movimientosDto> =
        api.getMovimientos(partidaId)

    suspend fun postMovimiento(movimiento: movimientosDto) =
        api.postMovimiento(movimiento)

    suspend fun createJugador(req: JugadorRequest): Resource<JugadorResponse> {
        return try {
            Log.d("RemoteDataSource", "Enviando request: nombres=${req.nombres}, email=${req.email}")
            val response = api.createJugador(req)
            Log.d("RemoteDataSource", "Respuesta HTTP: code=${response.code()}, isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                var body = response.body()
                Log.d("RemoteDataSource", "Body recibido: jugadorId=${body?.jugadorId}, nombres=${body?.nombres}, email=${body?.email}")

                if (body != null && body.jugadorId == null) {
                    val location = response.headers()["Location"]
                    Log.d("RemoteDataSource", "Location header: $location")

                    val idFromLocation = location?.let { obtenerIdLocation(it) }
                    if (idFromLocation != null) {
                        Log.d("RemoteDataSource", "ID extraido del Location: $idFromLocation")
                        body = body.copy(jugadorId = idFromLocation)
                    }
                }

                body?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Respuesta vacia del servidor")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("RemoteDataSource", "Error HTTP ${response.code()}: ${response.message()}, body: $errorBody")
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Excepcion al crear jugador", e)
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrio un error desconocido"}")
        }
    }

    private fun obtenerIdLocation(location: String): Int? {
        return try {
            val regex = """[?&]id=(\d+)""".toRegex()
            val matchResult = regex.find(location)
            matchResult?.groupValues?.get(1)?.toIntOrNull()
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error al extraer ID del Location", e)
            null
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