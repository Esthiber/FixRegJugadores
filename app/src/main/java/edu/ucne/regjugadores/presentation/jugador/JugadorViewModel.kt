package edu.ucne.regjugadores.presentation.jugador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.usecase.CreateJugadorLocalUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.DeleteJugadorUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.ObserveJugadorUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.TriggerSyncUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.UpsertJugadorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadorViewModel @Inject constructor(
    private val observeJugadorUseCase: ObserveJugadorUseCase,
    private val createLocalUseCase: CreateJugadorLocalUseCase,
    private val upsertUseCase: UpsertJugadorUseCase,
    private val deleteUseCase: DeleteJugadorUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(JugadorUiState(isLoading = true))
    val state: StateFlow<JugadorUiState> = _state.asStateFlow()

    init {
        observeJugadores()
    }

    private fun observeJugadores() {
        viewModelScope.launch {
            observeJugadorUseCase().collect { jugadores ->
                _state.update {
                    it.copy(
                        jugadores = jugadores,
                        isLoading = false
                    )
                }
            }
        }
    }

    suspend fun onEvent(event: JugadorEvent) {
        when (event) {
            is JugadorEvent.CrearJugador -> {
                crearJugador(event.nombre, event.email)
            }

            is JugadorEvent.UpdateJugador -> updateJugador(event.jugador)
            is JugadorEvent.DeleteJugador -> deleteJugador(event.id)

            is JugadorEvent.ShowCreateSheet -> {
                _state.value = _state.value.copy(showCreateSheet = true)
            }

            is JugadorEvent.HideCreateSheet -> {
                _state.value = _state.value.copy(showCreateSheet = false, jugadorNombre = "", jugadorEmail = "")
            }

            is JugadorEvent.OnNombreChange -> {
                _state.value = _state.value.copy(jugadorNombre = event.nombre)
            }

            is JugadorEvent.OnEmailChange -> {
                _state.value = _state.value.copy(jugadorEmail = event.email)
            }

            is JugadorEvent.UserMessageShown -> {
                _state.value = _state.value.copy(userMessage = null)
            }
        }
    }

    private suspend fun crearJugador(nombre: String, email: String) = viewModelScope.launch {
        val jugador = Jugador(nombres = nombre, email = email)
        when (val result = createLocalUseCase(jugador)) {
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        userMessage = "Jugador creado localmente",
                        showCreateSheet = false,
                        jugadorNombre = "",
                        jugadorEmail = ""
                    )
                }
                triggerSyncUseCase()
            }

            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            else -> {}
        }
    }

    private fun updateJugador(jugador: Jugador) = viewModelScope.launch {
        when (val result = upsertUseCase(jugador)) {
            is Resource.Success -> _state.update { it.copy(userMessage = "Jugador actualizado") }
            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            else -> {}
        }
    }

    private fun deleteJugador(id: String) = viewModelScope.launch {
        when (val result = deleteUseCase(id)) {
            is Resource.Success -> _state.update { it.copy(userMessage = "Jugador eliminado") }
            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            else -> {}
        }
    }

    private fun clearMessage() {
        _state.update { it.copy(userMessage = null) }
    }
}