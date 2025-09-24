package edu.ucne.regjugadores.presentation.partida.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.regjugadores.domain.partida.model.Partida
import edu.ucne.regjugadores.domain.partida.usecase.DeletePartidaUseCase
import edu.ucne.regjugadores.domain.partida.usecase.GetPartidaByIdUseCase
import edu.ucne.regjugadores.domain.partida.usecase.UpsertPartidaUseCase
import edu.ucne.regjugadores.domain.partida.usecase.validateJugador2
import kotlinx.coroutines.flow.MutableStateFlow
import edu.ucne.regjugadores.domain.jugador.usecase.ObserveJugadorUseCase
import edu.ucne.regjugadores.domain.partida.usecase.validateJugador1
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditPartidaViewModel @Inject constructor(
    private val getPartidaByIdUseCase: GetPartidaByIdUseCase,
    private val upsertPartidaUseCase: UpsertPartidaUseCase,
    private val deletePartidaUseCase: DeletePartidaUseCase,
    private val observeJugadorUseCase: ObserveJugadorUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(value = EditPartidaUiState())

    val state: StateFlow<EditPartidaUiState> = _state.asStateFlow()

    init {
        cargarJugadores()
    }

    fun onEvent(event: EditPartidaUiEvent) {
        when (event) {
            is EditPartidaUiEvent.Load -> onLoad(id = event.id)
            is EditPartidaUiEvent.FechaChanged -> _state.update {
                it.copy(fecha = state.value.fecha)
            }

            is EditPartidaUiEvent.Jugador1IDChanged -> _state.update {
                it.copy(jugador1ID = event.value, jugador1Error = null)
            }

            is EditPartidaUiEvent.Jugador2IDChanged -> _state.update {
                it.copy(jugador2ID = event.value, jugador2Error = null)
            }

            is EditPartidaUiEvent.GanadorIDChanged -> _state.update {
                it.copy(ganadorID = event.value)
            }

            is EditPartidaUiEvent.EsFinalizadaChanged -> _state.update {
                it.copy(esFinalizada = event.value)
            }

            is EditPartidaUiEvent.CargarJugadores -> cargarJugadores()

            EditPartidaUiEvent.Save -> {
                val currentJugadores = state.value.listaJugadores
                val isCurrentlyLoading = state.value.jugadoresLoading
                onSave()
                _state.update {
                    EditPartidaUiState(
                        listaJugadores = currentJugadores,
                        jugadoresLoading = isCurrentlyLoading
                    )
                }
            }

            EditPartidaUiEvent.Delete -> onDelete()

            EditPartidaUiEvent.Cancel -> {
                val currentJugadores = state.value.listaJugadores
                val isCurrentlyLoading = state.value.jugadoresLoading

                _state.update {
                    EditPartidaUiState(
                        listaJugadores = currentJugadores,
                        jugadoresLoading = isCurrentlyLoading
                    )
                }
            }
        }
    }

    private fun onLoad(id: Int?) {
        // Reset the saved flag whenever loading a new partida to prevent immediate closing
        _state.update {
            it.copy(
                saved = false,
                deleted = false,
                jugador1Error = null,
                jugador2Error = null
            )
        }

        if (id == null || id == 0) {
            _state.update {
                it.copy(
                    isNew = true,
                    partidaId = null,
                    jugador1ID = 0,
                    jugador2ID = 0,
                    ganadorID = null,
                    esFinalizada = false,
                    fecha = Date().toString()
                )
            }
            return
        }

        viewModelScope.launch {
            val partida = getPartidaByIdUseCase(id)
            if (partida != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        partidaId = partida.partidaId,
                        fecha = partida.fecha,
                        jugador1ID = partida.jugador1ID,
                        jugador2ID = partida.jugador2ID,
                        ganadorID = partida.ganadorID,
                        esFinalizada = partida.esFinalizada
                    )
                }
            }
        }
    }

    private fun onSave() {

        val currentJugadores = state.value.listaJugadores
        val isCurrentlyLoading = state.value.jugadoresLoading

        val jugador1ID = state.value.jugador1ID
        val jugador2ID = state.value.jugador2ID

        val jugador1Validations = validateJugador1(jugador1ID.toString(), jugador2ID.toString())
        val jugador2Validations = validateJugador2(jugador2ID.toString(), jugador1ID.toString())

        if (!jugador1Validations.isValid || !jugador2Validations.isValid) {
            _state.update {
                it.copy(
                    jugador1Error = jugador1Validations.error,
                    jugador2Error = jugador2Validations.error,

                )

//                EditPartidaUiState(
//                    listaJugadores = currentJugadores,
//                    jugadoresLoading = isCurrentlyLoading
//                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val id = state.value.partidaId ?: 0
            val partida = Partida(
                id,
                state.value.fecha,
                state.value.jugador1ID,
                state.value.jugador2ID,
                state.value.ganadorID,
                state.value.esFinalizada
            )
            val result = upsertPartidaUseCase(partida)
            result.onSuccess { newId ->
                _state.update { it.copy(isSaving = false, saved = true) }
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.partidaId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deletePartidaUseCase(id) // Todo Manejar resultado
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }

    private fun cargarJugadores() {
        viewModelScope.launch {
            _state.update { it.copy(jugadoresLoading = true) }
            observeJugadorUseCase().collect { jugadores ->
                _state.update { it.copy(jugadoresLoading = false, listaJugadores = jugadores) }
            }
        }
    }
}