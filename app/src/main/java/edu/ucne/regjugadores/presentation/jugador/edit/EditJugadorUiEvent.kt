package edu.ucne.regjugadores.presentation.jugador.edit

sealed interface EditJugadorUiEvent {
    data class Load(val id: Int?) : EditJugadorUiEvent

    data class NombresChanged(val value: String) : EditJugadorUiEvent
    data class PartidasChanged(val value: String) : EditJugadorUiEvent

    data object Save: EditJugadorUiEvent
    data object Delete: EditJugadorUiEvent
}