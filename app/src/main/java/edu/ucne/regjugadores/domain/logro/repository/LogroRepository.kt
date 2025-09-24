package edu.ucne.regjugadores.domain.logro.repository

import edu.ucne.regjugadores.domain.logro.model.Logro
import kotlinx.coroutines.flow.Flow

interface LogroRepository {
    fun observeLogros(): Flow<List<Logro>>
    suspend fun getLogroById(id: Int?): Logro?
    suspend fun upsert(logro: Logro): Int
    suspend fun deleteLogro(logro: Logro)
    suspend fun deleteLogroById(id: Int)
    suspend fun logroExisteByTitulo(titulo: String, idLogroActual: Int?): Boolean
}