package edu.ucne.regjugadores.data.jugadores.repository

import android.util.Log
import edu.ucne.regjugadores.data.jugadores.local.JugadorDao
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
            Log.d("SyncJugadores", "Sincronizando jugador ${req.nombres} ${req.email}")
            when (val result = remoteDataSource.createJugador(req)) {
                is Resource.Success -> {
                    val jugadorId = result.data?.jugadorId
                    if (jugadorId == null) {
                        Log.e("SyncJugadores", "API no devolvio jugadorId para ${jugador.nombres}")
                        return Resource.Error("API no devolvio el ID del jugador creado")
                    }
                    Log.d("SyncJugadores", "Jugador sincronizado con ID: $jugadorId")
                    val synced = jugador.copy(remoteId = jugadorId, isPendingCreate = false)
                    localDataSource.upsert(synced)
                    Log.d("SyncJugadores", "Sincronizado jugador ${jugador.nombres} con remoteId: $jugadorId")
                }

                is Resource.Error -> {
                    Log.e("SyncJugadores", "Fallo la sincronizacion de ${jugador.id} ${jugador.nombres} ${result.message}")
                    return Resource.Error("Fallo la sincronizacion")
                }
                else -> {
                    Log.e("SyncJugadores", "Error desconocido al sincronizar ${jugador.id} ${jugador.nombres}")
                }
            }
        }
        return Resource.Success(Unit)
    }
}