package edu.ucne.regjugadores.data.logros.repository

import edu.ucne.regjugadores.data.logros.local.LogroDao
import edu.ucne.regjugadores.data.logros.mapper.toDomain
import edu.ucne.regjugadores.data.logros.mapper.toEntity
import edu.ucne.regjugadores.domain.logro.model.Logro
import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogroRepositoryImpl @Inject constructor(
    private val dao: LogroDao
): LogroRepository {
    override fun observeLogros(): Flow<List<Logro>> = dao.ObserveAll().map{
            list -> list.map { it.toDomain()}
    }

    override suspend fun getLogroById(id: Int?): Logro? = dao.getById(id)?.toDomain()

    override suspend fun upsert(logro: Logro): Int {
        dao.upsert(logro.toEntity())
        return logro.logroId    }

    override suspend fun deleteLogro(logro: Logro) {
        dao.delete(logro.toEntity())
    }

    override suspend fun deleteLogroById(id: Int) {
       dao.deleteById(id)
    }

    override suspend fun logroExisteByTitulo(
        titulo: String,
        idLogroActual: Int?
    ): Boolean {
        return dao.existByTitle(titulo, idLogroActual)
    }

}