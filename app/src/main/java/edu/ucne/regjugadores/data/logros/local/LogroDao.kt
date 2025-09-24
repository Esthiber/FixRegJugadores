package edu.ucne.regjugadores.data.logros.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Query(value = "SELECT * FROM logros ORDER BY logroId DESC")
    fun ObserveAll(): Flow<List<LogroEntity>>

    @Query(value = "SELECT * FROM logros WHERE logroId = :id")
    suspend fun getById(id: Int?): LogroEntity?

    @Upsert
    suspend fun upsert(entity: LogroEntity)

    @Delete
    suspend fun delete(entity: LogroEntity)

    @Query(value = "DELETE FROM logros WHERE logroId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM logros WHERE LOWER(TRIM(titulo)) = LOWER(TRIM(:title)) AND (:idLogroActual IS NULL OR logroId != :idLogroActual))")
    suspend fun existByTitle(title: String, idLogroActual: Int?): Boolean
}