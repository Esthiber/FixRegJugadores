package edu.ucne.regjugadores.presentation.partida.list

import edu.ucne.regjugadores.domain.partida.model.Partida

data class ListPartidaUiState(
    val isLoading: Boolean = false,
    val partidas: List<Partida> = emptyList(),
    val message: String? = null,
    val navigationToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)
