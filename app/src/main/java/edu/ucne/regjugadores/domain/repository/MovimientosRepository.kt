package edu.ucne.regjugadores.domain.repository

import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.model.movimiento
import kotlinx.coroutines.flow.Flow

interface MovimientosRepository {
    fun getMovimientos(partidaId: Int): Flow<Resource<List<movimiento>>>
    suspend fun postMovimiento(movimiento: movimiento): Resource<Unit>
}