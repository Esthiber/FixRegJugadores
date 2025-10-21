package edu.ucne.regjugadores.presentation.tictactoe

sealed interface TicTacToeUiEvent {
    data class CellClicked(val row: Int, val col: Int) : TicTacToeUiEvent
    data class PartidaIdChanged(val value: String) : TicTacToeUiEvent
    data object RefreshMovimientos : TicTacToeUiEvent
    data class ShowMessage(val message: String?) : TicTacToeUiEvent
}