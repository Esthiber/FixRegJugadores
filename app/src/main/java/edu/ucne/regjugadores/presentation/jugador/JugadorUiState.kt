package edu.ucne.regjugadores.presentation.jugador

import edu.ucne.regjugadores.domain.jugador.model.Jugador

data class JugadorUiState(
    val isLoading: Boolean = false,
    val jugadores: List<Jugador> = emptyList(),
    val userMessage: String? = null,
    val showCreateSheet: Boolean = false,

    val jugadorNombre: String = "",
    val jugadorEmail: String = ""
)
