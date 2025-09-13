package edu.ucne.regjugadores.presentation.jugador.edit

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditJugadorScreen(
    viewModel: EditJugadorViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditJugadorBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun EditJugadorBody(
    state: EditJugadorUiState,
    onEvent: (EditJugadorUiEvent) -> Unit
){
    Column(
        modifier = Modifier
            .padding(16.dp)
    ){
        OutlinedTextField(
            value = state.nombres,
            onValueChange = {onEvent(EditJugadorUiEvent.NombresChanged(it)) },
            label = { Text("Nombres: ") },
            isError = state.nombreError != null,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_nombres")
        )
        if(state.nombreError != null){
            Text(
                text = state.nombreError,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.partidas,
            onValueChange = {onEvent(EditJugadorUiEvent.PartidasChanged(it)) },
            label = { Text("Partidas: ") },
            isError = state.partidasError != null,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_partidas"),
        )
        if(state.partidasError != null){
            Text(
                text = state.partidasError,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onEvent(EditJugadorUiEvent.Save)},
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_guardar")
        ) {
            Text("Guardar")
        }
    }
}

@Composable
private fun EditJugadorBodyPreview(){
    val state = EditJugadorUiState()
    MaterialTheme {
        EditJugadorBody(state = state) { }
    }
}