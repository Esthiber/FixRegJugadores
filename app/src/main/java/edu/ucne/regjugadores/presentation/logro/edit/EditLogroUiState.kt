package edu.ucne.regjugadores.presentation.logro.edit

data class EditLogroUiState(
    val logroId: Int? = null,
    val titulo: String = "",
    val descripcion: String = "",

    val tituloError: String? = null,
    val descripcionError: String? = null,

    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)
