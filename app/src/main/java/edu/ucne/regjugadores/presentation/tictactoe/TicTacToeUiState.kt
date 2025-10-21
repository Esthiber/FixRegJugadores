package edu.ucne.regjugadores.presentation.tictactoe

import edu.ucne.regjugadores.domain.model.movimiento

data class TicTacToeUiState(
    val partidaId: String = "",
    val tablero: List<List<String>> = List(3) { List(3) { "" } },
    val movimientos: List<movimiento> = emptyList(),
    val turnoActual: String = "X",
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
