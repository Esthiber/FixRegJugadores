package edu.ucne.regjugadores.domain.partida.repository

import edu.ucne.regjugadores.domain.partida.model.Partida
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {
    fun observePartida(): Flow<List<Partida>>
    suspend fun getPartidaById(id: Int?): Partida?
    suspend fun upsert(partida: Partida): Int
    suspend fun deletePartida(partida: Partida)
    suspend fun deletePartidaById(id: Int)
    suspend fun hasPartidas(jugadorId: Int): Boolean
}