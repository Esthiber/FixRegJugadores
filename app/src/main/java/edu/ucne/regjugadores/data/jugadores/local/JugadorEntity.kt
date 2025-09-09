package edu.ucne.regjugadores.data.jugadores.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jugadores")
class JugadorEntity (
    @PrimaryKey(autoGenerate = true)
    val jugadorId: Int = 0,
    val nombres: String = "",
    val partidas: Int = 0
)