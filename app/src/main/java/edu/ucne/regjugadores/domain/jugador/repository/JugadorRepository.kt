package edu.ucne.regjugadores.domain.jugador.repository

import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import kotlinx.coroutines.flow.Flow

interface JugadorRepository {
    fun observeJugador(): Flow<List<Jugador>>
    suspend fun getJugadorById(id: Int?): Jugador?
    suspend fun getJugadorById(id: String): Jugador?
    suspend fun createJugadorLocal(jugador: Jugador): Resource<Jugador>
    suspend fun upsert(jugador: Jugador): Resource<Unit>
    suspend fun deleteJugador(jugador: Jugador): Resource<Unit>
    suspend fun deleteJugadorById(id: String): Resource<Unit>
    suspend fun jugadorExisteByNombre(nombre: String, idJugadorActual: Int?): Boolean
    suspend fun postPendingJugadores(): Resource<Unit>
}