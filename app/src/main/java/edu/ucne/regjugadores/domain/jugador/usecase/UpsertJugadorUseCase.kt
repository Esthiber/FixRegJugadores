package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import javax.inject.Inject

class UpsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(jugador: Jugador): Resource<Unit> {
        val nombreResult = validateNombres(jugador.nombres)
        val emailResult = validateEmail(jugador.email)

        if (!nombreResult.isValid) {
            return Resource.Error(IllegalArgumentException(nombreResult.error).toString())
        }
        if (!emailResult.isValid) {
            return Resource.Error(IllegalArgumentException(emailResult.error).toString())
        }
        return repository.upsert(jugador)
    }
}