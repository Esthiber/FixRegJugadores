package edu.ucne.regjugadores.presentation.logro.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.regjugadores.domain.logro.model.Logro
import edu.ucne.regjugadores.domain.logro.usecase.DeleteLogroUseCase
import edu.ucne.regjugadores.domain.logro.usecase.GetLogroByIdUseCase
import edu.ucne.regjugadores.domain.logro.usecase.LogroExisteUseCase
import edu.ucne.regjugadores.domain.logro.usecase.UpsertLogroUseCase
import edu.ucne.regjugadores.domain.logro.usecase.ValidarDescripcion
import edu.ucne.regjugadores.domain.logro.usecase.ValidarTitulo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLogroViewModel @Inject constructor(
    private val getLogroByIdUseCase: GetLogroByIdUseCase,
    private val upsertLogroUseCase: UpsertLogroUseCase,
    private val deleteLogroUseCase: DeleteLogroUseCase,
    private val logroExisteUseCase: LogroExisteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(value = EditLogroUiState())

    val state: StateFlow<EditLogroUiState> = _state.asStateFlow()

    fun onEvent(event: EditLogroUiEvent) {
        when (event) {
            is EditLogroUiEvent.Load -> onLoad(id = event.id)
            is EditLogroUiEvent.TituloChanged -> _state.update {
                it.copy(titulo = event.value, tituloError = null)
            }

            is EditLogroUiEvent.DescripcionChanged -> _state.update {
                it.copy(descripcion = event.value, descripcionError = null)
            }

            EditLogroUiEvent.Save -> onSave()
            EditLogroUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?){
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, logroId = null) }
            return
        }
        viewModelScope.launch {
            val logro = getLogroByIdUseCase(id)
            if (logro != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        logroId = logro.logroId,
                        titulo = logro.titulo,
                        descripcion = logro.descripcion
                    )
                }
            }
        }
    }

    private fun onSave() {
        val titulo = state.value.titulo
        val descripcion = state.value.descripcion
        val titluloValidation = ValidarTitulo(titulo)
        val descripcionValidation = ValidarDescripcion(descripcion)

        if (!titluloValidation.isValid || !descripcionValidation.isValid) {
            _state.update {
                it.copy(
                    tituloError = titluloValidation.error,
                    descripcionError = descripcionValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            val existe = logroExisteUseCase(titulo, state.value.logroId ?: 0)
            if (existe) {
                _state.update {
                    it.copy(
                        tituloError = "Ya existe un logro con este titulo"
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            val id = state.value.logroId ?: 0
            val logro = Logro(
                id,
                state.value.titulo,
                state.value.descripcion
            )
            val result = upsertLogroUseCase(logro)

            result.onSuccess { newId ->
                _state.value = EditLogroUiState()
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false) }
            }
        }

    }

    private fun onDelete() {
        val id = state.value.logroId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteLogroUseCase(id)
            _state.update { it.copy(deleted = true) }
        }
    }
}