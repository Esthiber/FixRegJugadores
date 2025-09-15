package edu.ucne.regjugadores.presentation.partida.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.regjugadores.domain.partida.model.Partida

@Composable
fun ListPartidaScreen(
    viewModel: ListPartidaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListPartidaBody(state, viewModel::onEvent)
}

@Composable
fun ListPartidaBody(
    state: ListPartidaUiState,
    onEvent: (ListPartidaUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.Center)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("Lista_Partidas")
        ) {
            items(state.partidas) { partida ->
                PartidaCard(
                    partida = partida,
                    onEdit = { onEvent(ListPartidaUiEvent.Edit(partida.partidaId)) },
                    onDelete = { onEvent(ListPartidaUiEvent.Delete(partida.partidaId)) }
                )
            }
        }
    }
}

@Composable
fun PartidaCard(
    partida: Partida, onEdit: () -> Unit, onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("PartidaCard_${partida.partidaId}")
            .clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${partida.partidaId}")
                Text(text = "${partida.jugador1ID}")
                Text(text = "${partida.jugador2ID}")
                Text(text = "${partida.ganadorID}")
                Text(text = partida.fecha)
            }
            TextButton(
                onClick = onEdit,
                modifier = Modifier.testTag("Editbutton_${partida.partidaId}")
            ) {
                Text("Editar")
            }
            TextButton(
                onClick = onDelete,
                modifier = Modifier
                    .testTag("delete_button_${partida.partidaId}")
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Preview
@Composable
fun PreviewListPartidaScreen() {
    val state = ListPartidaUiState()
    ListPartidaBody(state = state, onEvent = {})
}