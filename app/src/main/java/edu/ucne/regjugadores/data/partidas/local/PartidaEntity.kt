package edu.ucne.regjugadores.data.partidas.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "partidas",
//    foreignKeys = [
//        ForeignKey (
//            entity = JugadorEntity::class,
//            parentColumns = ["jugadorId"],
//            childColumns = ["jugador1ID"],
//            onDelete = ForeignKey.RESTRICT
//        ),
//        ForeignKey (
//            entity = JugadorEntity::class,
//            parentColumns = ["jugadorId"],
//            childColumns = ["jugador2ID"],
//            onDelete = ForeignKey.RESTRICT
//        ),
//        ForeignKey (
//            entity = JugadorEntity::class,
//            parentColumns = ["jugadorId"],
//            childColumns = ["ganadorID"],
//            onDelete = ForeignKey.RESTRICT
//        ),
//    ]
)
class PartidaEntity(
    @PrimaryKey(autoGenerate = true)
    val partidaId: Int = 0,
    val fecha: String,
    val jugador1ID: Int = 0,
    val jugador2ID: Int = 0,
    val ganadorID: Int?,
    val esFinalizada: Boolean = false,
)