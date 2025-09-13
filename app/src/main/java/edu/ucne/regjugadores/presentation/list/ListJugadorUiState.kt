package edu.ucne.regjugadores.presentation.list

import edu.ucne.regjugadores.domain.jugador.model.Jugador

data class ListJugadorUiState(
    val isLoading: Boolean = false,
    val jugadores: List<Jugador> = emptyList(),
    val message: String? = null,
    val navigationToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)
