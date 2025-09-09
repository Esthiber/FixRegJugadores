package edu.ucne.regjugadores.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.regjugadores.domain.model.Jugador

@Composable
fun ListJugadoresScreen(
    viewModel: ListJugadorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListJugadorBody(state, viewModel::onEvent)
}

@Composable
fun ListJugadorBody(
    state: ListJugadorUiState,
    onEvent: (ListJugadorUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("loading")
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("Lista_Jugadores")
        ) {
            items(state.jugadores) { jugador ->
                JugadorCard(
                    jugador = jugador,
                    onEdit = { onEvent(ListJugadorUiEvent.Edit(jugador.JugadorId)) },
                    onDelete = { onEvent(ListJugadorUiEvent.Delete(jugador.JugadorId)) }
                )
            }
        }
    }


}
@Composable
fun JugadorCard(
    jugador: Jugador, onEdit: () -> Unit, onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("JugadorCard_${jugador.JugadorId}")
            .clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(1f)){
                Text(jugador.Nombres,style = MaterialTheme.typography.h3)
                Text("Partidas: ${jugador.Partidas}")
            }
            TextButton(
                onClick = onEdit,
                modifier = Modifier.testTag("Editbutton_${jugador.JugadorId}")
            ){
                Text("Editar")
            }
            TextButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_button_${jugador.JugadorId}")
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Preview
@Composable
private fun ListJugadorBodyPreview(){
    MaterialTheme {
        val state = ListJugadorUiState()
        ListJugadorBody(state) { }
    }
}