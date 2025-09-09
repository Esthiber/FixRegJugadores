package edu.ucne.regjugadores.presentation.list

interface ListJugadorUiEvent {
    data object Load : ListJugadorUiEvent
    data class Delete(val id: Int) : ListJugadorUiEvent
    data object CreateNew : ListJugadorUiEvent
    data class Edit(val id: Int) : ListJugadorUiEvent
    data class ShowMessage(val message: String?) : ListJugadorUiEvent
}