package edu.ucne.regjugadores.presentation.logro.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditLogroScreen(
    viewModel: EditLogroViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditLogroBody(
        state,
        viewModel::onEvent
    )
}

@Composable
fun EditLogroBody(
    state: EditLogroUiState,
    onEvent: (EditLogroUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.titulo,
            onValueChange = { onEvent(EditLogroUiEvent.TituloChanged(it)) },
            label = { Text("Título") },
            isError = state.tituloError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.tituloError != null) {
            Text(
                text = state.tituloError,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.descripcion,
            onValueChange = { onEvent(EditLogroUiEvent.DescripcionChanged(it)) },
            label = { Text("Descripción") },
            isError = state.descripcionError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.descripcionError != null) {
            Text(
                text = state.descripcionError,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onEvent(EditLogroUiEvent.Save) },
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
        if (!state.isNew) {
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onEvent(EditLogroUiEvent.Delete) },
                enabled = !state.isDeleting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Preview
@Composable
private fun EditLogroBodyPreview() {
    val state = remember { EditLogroUiState() }
    MaterialTheme {
        EditLogroBody(state) {}
    }
}