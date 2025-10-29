package edu.ucne.regjugadores.presentation.jugador

import edu.ucne.regjugadores.domain.jugador.model.Jugador

sealed interface JugadorEvent {
    data class CrearJugador(val nombre: String, val email: String) : JugadorEvent
    data class UpdateJugador(val jugador: Jugador) : JugadorEvent
    data class DeleteJugador(val id: String) : JugadorEvent

    object ShowCreateSheet : JugadorEvent
    object HideCreateSheet : JugadorEvent

    data class OnNombreChange(val nombre: String) : JugadorEvent
    data class OnEmailChange(val email: String) : JugadorEvent

    object UserMessageShown : JugadorEvent
}