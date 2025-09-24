package edu.ucne.regjugadores.presentation.logro.list

import edu.ucne.regjugadores.domain.logro.model.Logro

data class ListLogroUiState(
    val isLoading: Boolean = false,
    val logros: List<Logro> = emptyList(),
    val message: String? = null,
    val navigationToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)
