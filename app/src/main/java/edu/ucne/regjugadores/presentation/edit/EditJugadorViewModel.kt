package edu.ucne.regjugadores.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.regjugadores.domain.model.Jugador
import edu.ucne.regjugadores.domain.usecase.DeleteJugadorUseCase
import edu.ucne.regjugadores.domain.usecase.GetJugadorByIdUseCase
import edu.ucne.regjugadores.domain.usecase.JugadorExisteUseCase
import edu.ucne.regjugadores.domain.usecase.UpsertJugadorUseCase
import edu.ucne.regjugadores.domain.usecase.validateNombres
import edu.ucne.regjugadores.domain.usecase.validatePartidas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditJugadorViewModel @Inject constructor(
    private val getJugadorByIdUseCase: GetJugadorByIdUseCase,
    private val upsertJugadorUseCase: UpsertJugadorUseCase,
    private val deleteJugadorUseCase: DeleteJugadorUseCase,
    private val existeJugadorByNombreUseCase: JugadorExisteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(value = EditJugadorUiState())

    val state: StateFlow<EditJugadorUiState> = _state.asStateFlow()

    fun onEvent(event: EditJugadorUiEvent) {
        when (event) {
            is EditJugadorUiEvent.Load -> onLoad(id = event.id)
            is EditJugadorUiEvent.NombresChanged -> _state.update {
                it.copy(nombres = event.value, nombreError = null)
            }

            is EditJugadorUiEvent.PartidasChanged -> _state.update {
                it.copy(partidas = event.value, partidasError = null)
            }

            EditJugadorUiEvent.Save -> onSave()
            EditJugadorUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, jugadorId = null) }
            return
        }
        viewModelScope.launch {
            val jugador = getJugadorByIdUseCase(id)
            if (jugador != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        jugadorId = jugador.JugadorId,
                        nombres = jugador.Nombres,
                        partidas = jugador.Partidas.toString()
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombres = state.value.nombres
        val nombresValidations = validateNombres(nombres)
        val partidas = state.value.partidas
        val partidasValidations = validatePartidas(partidas)

        if (!nombresValidations.isValid || !partidasValidations.isValid) {
            _state.update {
                it.copy(
                    nombreError = nombresValidations.error,
                    partidasError = partidasValidations.error
                )
            }
            return
        }

        viewModelScope.launch {
            val currentId = state.value.jugadorId
            if (existeJugadorByNombreUseCase(nombres, currentId)) {
                _state.update {
                    it.copy(
                        nombreError = "Ya hay alguien con ese nombre"
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            val id = state.value.jugadorId ?: 0
            val jugador = Jugador(
                id,
                state.value.nombres,
                state.value.partidas.toInt()
            )
            val result = upsertJugadorUseCase(jugador)
            result.onSuccess { newId ->
                _state.value = EditJugadorUiState()
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete(){
        val id = state.value.jugadorId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true)}
            deleteJugadorUseCase(id) // TODO despues de la entrega manejar resultado o error. Nose, me parece conveniente.
            _state.update {it.copy(isDeleting = false, deleted = true)}
        }
    }

}