package edu.ucne.regjugadores.presentation.logro.list

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
import androidx.compose.material3.MaterialTheme
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
import edu.ucne.regjugadores.domain.logro.model.Logro

@Composable
fun ListLogrosScreen(
    viewModel: ListLogroViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListLogroBody(state, viewModel::onEvent)
}

@Composable
fun ListLogroBody(
    state: ListLogroUiState,
    onEvent: (ListLogroUiEvent) -> Unit
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
                .testTag("Lista_Logros")
        ) {
            items(state.logros) { logro ->
                LogroCard(
                    logro = logro,
                    onEdit = { onEvent(ListLogroUiEvent.Edit(logro.logroId)) },
                    onDelete = { onEvent(ListLogroUiEvent.Delete(logro.logroId)) }
                )
            }
        }
    }
}

@Composable
fun LogroCard(
    logro: Logro, onEdit: () -> Unit, onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("LogroCard_${logro.logroId}")
            .clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(1f)){
                Text(logro.titulo, style = MaterialTheme.typography.titleMedium)
                Text(logro.descripcion, style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(
                onClick = onEdit,
                modifier = Modifier.testTag("Editbutton_${logro.logroId}")
            ){
                Text("Editar")
            }
            TextButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_button_${logro.logroId}")
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Preview
@Composable
private fun ListLogroBodyPreview(){
    MaterialTheme {
        val state = ListLogroUiState()
        ListLogroBody(state) { }
    }
}
