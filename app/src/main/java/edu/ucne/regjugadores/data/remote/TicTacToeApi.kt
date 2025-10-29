package edu.ucne.regjugadores.data.remote

import edu.ucne.regjugadores.data.remote.dto.movimientosDto
import edu.ucne.regjugadores.data.remote.dto.request.jugadores.JugadorRequest
import edu.ucne.regjugadores.data.remote.dto.response.jugadores.JugadorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicTacToeApi {

    /// Movimientos
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") id: Int): List<movimientosDto>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: movimientosDto): Unit

    /// Jugadores

    @POST("api/Jugadores")
    suspend fun createJugador(@Body req: JugadorRequest): Response<JugadorResponse>

    @PUT("api/Jugadores/{jugadorId}")
    suspend fun updateJugador(@Path("jugadorId") id: Int, @Body req: JugadorRequest): Response<Unit>

    @DELETE("api/Jugadores/{jugadorId}")
    suspend fun deleteJugador(@Path("jugadorId") id: Int): Response<Unit>
}