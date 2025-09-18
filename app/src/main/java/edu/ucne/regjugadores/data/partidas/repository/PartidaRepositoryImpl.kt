package edu.ucne.regjugadores.data.partidas.repository

import edu.ucne.regjugadores.data.partidas.local.PartidaDao
import edu.ucne.regjugadores.data.partidas.mapper.toDomain
import edu.ucne.regjugadores.data.partidas.mapper.toEntity
import edu.ucne.regjugadores.domain.partida.model.Partida
import edu.ucne.regjugadores.domain.partida.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val dao: PartidaDao
) : PartidaRepository {
    override fun observePartida(): Flow<List<Partida>> = dao.ObserveAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getPartidaById(id: Int?): Partida? = dao.getById(id)?.toDomain()

    override suspend fun upsert(partida: Partida): Int {
        dao.upsert(partida.toEntity())
        return partida.partidaId
    }

    override suspend fun deletePartida(partida: Partida) {
        dao.delete(partida.toEntity())
    }

    override suspend fun deletePartidaById(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun hasPartidas(jugadorId: Int): Boolean {
        return dao.countPartidasByJugadorId(jugadorId) > 0
    }
}