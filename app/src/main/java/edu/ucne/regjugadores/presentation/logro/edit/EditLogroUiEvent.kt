package edu.ucne.regjugadores.presentation.logro.edit

sealed interface EditLogroUiEvent {
    data class Load(val id: Int?) : EditLogroUiEvent

    data class DescripcionChanged(val value: String) : EditLogroUiEvent
    data class TituloChanged(val value: String) : EditLogroUiEvent

    data object  Save: EditLogroUiEvent
    data object  Delete: EditLogroUiEvent
}