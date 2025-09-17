package edu.ucne.regjugadores.presentation.partida.edit

import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorUiEvent

sealed interface EditPartidaUiEvent {
    data class Load(val id:Int?) : EditPartidaUiEvent
    data class FechaChanged(val value: String) : EditPartidaUiEvent
    data class Jugador1IDChanged(val value: Int) : EditPartidaUiEvent
    data class Jugador2IDChanged(val value: Int) : EditPartidaUiEvent
    data class GanadorIDChanged(val value: Int?) : EditPartidaUiEvent
    data class EsFinalizadaChanged(val value: Boolean) : EditPartidaUiEvent

    data class CargarJugadores(val value: String) : EditPartidaUiEvent

    data object Save: EditPartidaUiEvent
    data object Delete: EditPartidaUiEvent
    data object Cancel: EditPartidaUiEvent
}