package edu.ucne.regjugadores.presentation.partida.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditPartidaScreen(
    viewModel: EditPartidaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditPartidaBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}
@Composable
private fun EditPartidaBody(
    state: EditPartidaUiState,
    onEvent: (EditPartidaUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = state.fecha
        )

        Text(
            text = state.jugador1ID.toString()
        )
        Text(
            text = state.jugador2ID.toString()
        )
        Text(
            text = state.ganadorID?.toString() ?: "N/A"
        )

        Text("Compilando...")
        if(state.jugador1Error != null){
            Text(
                text = state.jugador1Error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
fun EditPartidaBodyPreview() {
    val state  = EditPartidaUiState()
    MaterialTheme{
        EditPartidaBody(state = state) { }
    }
}