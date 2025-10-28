package edu.ucne.regjugadores.data.jugadores.repository

import edu.ucne.regjugadores.data.jugadores.local.JugadorDao
import edu.ucne.regjugadores.data.jugadores.local.JugadorEntity
import edu.ucne.regjugadores.data.jugadores.mapper.toDomain
import edu.ucne.regjugadores.data.jugadores.mapper.toEntity
import edu.ucne.regjugadores.data.remote.RemoteDataSource
import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.data.remote.dto.request.jugadores.JugadorRequest
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val localDataSource: JugadorDao,
    private val remoteDataSource: RemoteDataSource
) : JugadorRepository {
    override fun observeJugador(): Flow<List<Jugador>> = localDataSource.ObserveAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getJugadorById(id: Int?): Jugador? =
        localDataSource.getById(id)?.toDomain()

    override suspend fun getJugadorById(id: String): Jugador? =
        localDataSource.getById(id)?.toDomain()

    override suspend fun createJugadorLocal(jugador: Jugador): Resource<Jugador> {
       val pending = jugador.copy(isPendingCreate = true)
         localDataSource.upsert(pending.toEntity())
        return Resource.Success(pending)
    }

    override suspend fun upsert(jugador: Jugador): Resource<Unit> {
        val remoteId = jugador.remoteId ?: return Resource.Error("No remoteId")
        val req = JugadorRequest(nombres = jugador.nombres, email = jugador.email)
        return when (val result = remoteDataSource.updateJugador(remoteId, req)) {
            is Resource.Success -> {
                localDataSource.upsert(jugador.toEntity())
                Resource.Success(Unit)
            }

            is Resource.Error -> result
            else -> Resource.Loading()
        }
    }

    override suspend fun deleteJugador(jugador: Jugador): Resource<Unit> {
        val jugador = localDataSource.getById(jugador.id)
            ?: return Resource.Error("No se encontro jugador con id $jugador.id")
        val remoteId = jugador.remoteId ?: return Resource.Error("Jugador no tiene remoteId")
        return when (val result = remoteDataSource.deleteJugador(remoteId)) {
            is Resource.Success -> {
                localDataSource.delete(jugador)
                Resource.Success(Unit)
            }

            is Resource.Error -> result
            else -> Resource.Loading()
        }
    }

    override suspend fun deleteJugadorById(id: String): Resource<Unit> {
        val jugador = localDataSource.getById(id)
            ?: return Resource.Error("No se encontro jugador con id $id")
        val remoteId = jugador.remoteId ?: return Resource.Error("Jugador no tiene remoteId")
        return when (val result = remoteDataSource.deleteJugador(remoteId)) {
            is Resource.Success -> {
                localDataSource.deleteById(id)
                Resource.Success(Unit)
            }

            is Resource.Error -> result
            else -> Resource.Loading()
        }
    }

    override suspend fun jugadorExisteByNombre(nombre: String, idJugadorActual: Int?): Boolean {
        return localDataSource.existByName(nombre, idJugadorActual)
    }

    override suspend fun postPendingJugadores(): Resource<Unit> {
        val pending = localDataSource.getPendingCreateJugadores()
        for (jugador in pending) {
            val req = JugadorRequest(jugador.nombres, jugador.email)
            when (val result = remoteDataSource.createJugador(req)) {
                is Resource.Success -> {
                    val synced =
                        JugadorEntity(// TODO Ojo, no funciono el 'jugador.copy', revisar esta alternativa.
                            id = jugador.id,
                            remoteId = result.data?.jugadorId,
                            nombres = jugador.nombres,
                            email = jugador.email,
                            isPendingCreate = false
                        )
                    localDataSource.upsert(synced)
                }

                is Resource.Error -> return Resource.Error("Fallo la sincronizacion")
                else -> {}
            }
        }
        return Resource.Success(Unit)
    }
}