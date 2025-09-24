package edu.ucne.regjugadores.presentation.logro.list

interface ListLogroUiEvent {
    data object Load : ListLogroUiEvent
    data class Delete(val id: Int) : ListLogroUiEvent
    data object CreateNew : ListLogroUiEvent
    data class Edit(val id: Int) : ListLogroUiEvent
    data class ShowMessage(val message: String?) : ListLogroUiEvent
}