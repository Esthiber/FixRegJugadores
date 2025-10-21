package edu.ucne.regjugadores.presentation.tictactoe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.model.movimiento
import edu.ucne.regjugadores.domain.repository.MovimientosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val movimientosRepository: MovimientosRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TicTacToeUiState())
    val state: StateFlow<TicTacToeUiState> = _state.asStateFlow()

    fun onEvent(event: TicTacToeUiEvent) {
        when (event) {
            is TicTacToeUiEvent.PartidaIdChanged -> {
                _state.update { it.copy(partidaId = event.value) }
            }
            is TicTacToeUiEvent.RefreshMovimientos -> {
                refreshMovimientos()
            }
            is TicTacToeUiEvent.CellClicked -> {
                postMovimiento(event.row, event.col)
            }
            is TicTacToeUiEvent.ShowMessage -> {
                _state.update { it.copy(message = event.message) }
            }
        }
    }

    private fun refreshMovimientos() {
        val partidaIdInt = _state.value.partidaId.toIntOrNull()
        if (partidaIdInt == null) {
            _state.update { it.copy(error = "ID de partida inválido") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            movimientosRepository.getMovimientos(partidaIdInt).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val movimientos = resource.data ?: emptyList()
                        val nuevoTablero = crearTableroDesdeMovimientos(movimientos)
                        _state.update {
                            it.copy(
                                movimientos = movimientos,
                                tablero = nuevoTablero,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message ?: "Error al cargar movimientos"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun postMovimiento(row: Int, col: Int) {
        val partidaIdInt = _state.value.partidaId.toIntOrNull()
        if (partidaIdInt == null) {
            _state.update { it.copy(error = "ID de partida inválido") }
            return
        }
        if (_state.value.tablero[row][col].isNotEmpty()) {
            _state.update { it.copy(message = "Esta celda ya está ocupada") }
            return
        }

        val nuevoMovimiento = movimiento(
            partidaId = partidaIdInt,
            jugador = _state.value.turnoActual,
            posicionFila = row,
            posicionColumna = col
        )

        viewModelScope.launch {
            when (val result = movimientosRepository.postMovimiento(nuevoMovimiento)) {
                is Resource.Success -> {
                    val nuevoTablero = _state.value.tablero.map { fila ->
                        fila.toMutableList()
                    }.toMutableList()
                    nuevoTablero[row][col] = _state.value.turnoActual

                    val siguienteTurno = if (_state.value.turnoActual == "X") "O" else "X"

                    _state.update {
                        it.copy(
                            tablero = nuevoTablero,
                            turnoActual = siguienteTurno,
                            message = "Movimiento realizado exitosamente"
                        )
                    }

                    refreshMovimientos()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(error = result.message ?: "Error al realizar movimiento")
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun crearTableroDesdeMovimientos(movimientos: List<movimiento>): List<List<String>> {
        val tablero = MutableList(3) { MutableList(3) { "" } }

        movimientos.forEach { mov ->
            if (mov.posicionFila in 0..2 && mov.posicionColumna in 0..2) {
                tablero[mov.posicionFila][mov.posicionColumna] = mov.jugador
            }
        }
        return tablero
    }
}
