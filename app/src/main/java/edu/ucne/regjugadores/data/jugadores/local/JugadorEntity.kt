package edu.ucne.regjugadores.data.jugadores.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "jugadores")
data class JugadorEntity (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombres: String,
    val email: String,
    val isPendingCreate: Boolean = false,
)