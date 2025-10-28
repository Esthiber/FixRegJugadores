package edu.ucne.regjugadores.data.jugadores.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Query(value = "SELECT * FROM jugadores ORDER BY id DESC")
    fun ObserveAll(): Flow<List<JugadorEntity>>

    @Query(value = "SELECT * FROM jugadores WHERE id = :id")
    suspend fun getById(id: Int?): JugadorEntity?

    @Query(value = "SELECT * FROM jugadores WHERE id = :id")
    suspend fun getById(id: String): JugadorEntity?

    @Upsert
    suspend fun upsert(entity: JugadorEntity)

    @Delete
    suspend fun delete(entity: JugadorEntity)

    @Query(value = "DELETE FROM jugadores WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query(value = "DELETE FROM jugadores WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM jugadores WHERE LOWER(TRIM(nombres)) = LOWER(TRIM(:nombre)) AND (:idJugadorActual IS NULL OR id != :idJugadorActual))")
    suspend fun existByName(nombre: String, idJugadorActual: Int?): Boolean

    @Query("SELECT * FROM jugadores WHERE isPendingCreate = 1")
    suspend fun getPendingCreateJugadores(): List<JugadorEntity>
}