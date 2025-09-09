package edu.ucne.regjugadores.domain.repository

import edu.ucne.regjugadores.domain.model.Jugador
import kotlinx.coroutines.flow.Flow

interface JugadorRepository {
    fun observeJugador(): Flow<List<Jugador>>
    suspend fun getJugadorById(id: Int?): Jugador?
    suspend fun upsert(jugador: Jugador): Int
    suspend fun deleteJugador(jugador: Jugador)
    suspend fun deleteJugadorbyId(id: Int)
    suspend fun jugadorExisteByNombre(nombre: String, idJugadorActual: Int?): Boolean
}