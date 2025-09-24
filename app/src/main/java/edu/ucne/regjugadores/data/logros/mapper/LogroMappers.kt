package edu.ucne.regjugadores.data.logros.mapper

import edu.ucne.regjugadores.data.logros.local.LogroEntity
import edu.ucne.regjugadores.domain.logro.model.Logro

fun LogroEntity.toDomain(): Logro = Logro(
    logroId = logroId,
    titulo = titulo,
    descripcion = descripcion
)

fun Logro.toEntity(): LogroEntity = LogroEntity(
    logroId = logroId,
    titulo = titulo,
    descripcion = descripcion
)