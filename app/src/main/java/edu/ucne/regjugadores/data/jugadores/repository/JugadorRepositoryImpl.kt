package edu.ucne.regjugadores.data.jugadores.repository

import edu.ucne.regjugadores.data.jugadores.local.JugadorDao
import edu.ucne.regjugadores.data.jugadores.mapper.toDomain
import edu.ucne.regjugadores.data.jugadores.mapper.toEntity
import edu.ucne.regjugadores.domain.model.Jugador
import edu.ucne.regjugadores.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val dao: JugadorDao
): JugadorRepository {
    override fun observeJugador(): Flow<List<Jugador>> = dao.ObserveAll().map{
        list -> list.map { it.toDomain()}
    }

    override suspend fun getJugadorById(id: Int?): Jugador? = dao.getById(id)?.toDomain()

    override suspend fun upsert(jugador: Jugador):Int{
        dao.upsert(jugador.toEntity())
        return jugador.JugadorId
    }

    override suspend fun deleteJugador(jugador:Jugador){
        dao.delete(jugador.toEntity())
    }

    override suspend fun deleteJugadorbyId(id:Int){
        dao.deleteById(id)
    }
    override suspend fun jugadorExisteByNombre(nombre: String,idJugadorActual:Int?):Boolean{
        return dao.existByName(nombre,idJugadorActual)
    }
}