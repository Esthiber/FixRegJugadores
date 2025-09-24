package edu.ucne.regjugadores.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.regjugadores.data.jugadores.local.JugadorDao
import edu.ucne.regjugadores.data.jugadores.local.JugadorEntity
import edu.ucne.regjugadores.data.logros.local.LogroDao
import edu.ucne.regjugadores.data.logros.local.LogroEntity
import edu.ucne.regjugadores.data.partidas.local.PartidaDao
import edu.ucne.regjugadores.data.partidas.local.PartidaEntity

@Database(
    entities = [
        JugadorEntity::class,
        PartidaEntity::class,
        LogroEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class JugadorDB : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao

    abstract fun partidaDao(): PartidaDao

    abstract fun logroDao(): LogroDao
}