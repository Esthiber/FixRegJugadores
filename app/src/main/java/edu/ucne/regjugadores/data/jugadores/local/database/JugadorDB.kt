package edu.ucne.regjugadores.data.jugadores.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.regjugadores.data.jugadores.local.JugadorDao
import edu.ucne.regjugadores.data.jugadores.local.JugadorEntity

@Database(
    entities = [
        JugadorEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class JugadorDB : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
}