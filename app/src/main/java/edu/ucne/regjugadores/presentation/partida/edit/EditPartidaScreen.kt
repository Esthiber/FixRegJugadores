package edu.ucne.regjugadores.presentation.partida.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditPartidaScreen(
    viewModel: EditPartidaViewModel = hiltViewModel(),
    onCancel: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) {
            onSaveSuccess()
        }
    }

    EditPartidaBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPartidaBody(
    state: EditPartidaUiState,
    onEvent: (EditPartidaUiEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        if (state.jugadoresLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Fecha display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Fecha: ${state.fecha}", // TODO Formatear fecha, al espanol preferiblemente.
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Jugador 1 SelectBox
        var jugador1Expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = jugador1Expanded,
            onExpandedChange = { jugador1Expanded = it }
        ) {
            OutlinedTextField(
                value = state.listaJugadores.find { it.JugadorId == state.jugador1ID }?.Nombres ?: "Seleccione Jugador 1",
                onValueChange = {},
                readOnly = true,
                label = { Text("Jugador 1") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jugador1Expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = jugador1Expanded,
                onDismissRequest = { jugador1Expanded = false }
            ) {
                state.listaJugadores.forEach { jugador ->
                    DropdownMenuItem(
                        text = { Text(jugador.Nombres) },
                        onClick = {
                            onEvent(EditPartidaUiEvent.Jugador1IDChanged(jugador.JugadorId))
                            jugador1Expanded = false
                        }
                    )
                }
            }
        }

        // Mensaje de Validación para jugador 1
        if (state.jugador1Error != null) {
            Text(
                text = state.jugador1Error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Jugador 2 SelectBox
        var jugador2Expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = jugador2Expanded,
            onExpandedChange = { jugador2Expanded = it }
        ) {
            OutlinedTextField(
                value = state.listaJugadores.find { it.JugadorId == state.jugador2ID }?.Nombres ?: "Seleccione Jugador 2",
                onValueChange = {},
                readOnly = true,
                label = { Text("Jugador 2") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jugador2Expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = jugador2Expanded,
                onDismissRequest = { jugador2Expanded = false }
            ) {
                state.listaJugadores.forEach { jugador ->
                    DropdownMenuItem(
                        text = { Text(jugador.Nombres) },
                        onClick = {
                            onEvent(EditPartidaUiEvent.Jugador2IDChanged(jugador.JugadorId))
                            jugador2Expanded = false
                        }
                    )
                }
            }
        }

        // Mensaje de Validación para jugador 2
        if (state.jugador2Error != null) {
            Text(
                text = state.jugador2Error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val ganadorSeleccionable = state.jugador1ID != null && state.jugador2ID != null
        val ganadorID = state.ganadorID
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Selecciona el ganador:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Button(
                onClick = { onEvent(EditPartidaUiEvent.GanadorIDChanged(state.jugador1ID!!)) },
                enabled = ganadorSeleccionable,
                modifier = Modifier
                    .fillMaxWidth()
                    .let {
                        if (ganadorID == state.jugador1ID) it.then(Modifier) else it
                    },
                colors = if (ganadorID == state.jugador1ID)
                    androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                else
                    androidx.compose.material3.ButtonDefaults.buttonColors()
            ) {
                Text(
                    text = state.listaJugadores.find { it.JugadorId == state.jugador1ID }?.Nombres ?: "Jugador 1",
                    color = if (ganadorID == state.jugador1ID) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Button(
                onClick = { onEvent(EditPartidaUiEvent.GanadorIDChanged(state.jugador2ID!!)) },
                enabled = ganadorSeleccionable,
                modifier = Modifier
                    .fillMaxWidth()
                    .let {
                        if (ganadorID == state.jugador2ID) it.then(Modifier) else it
                    },
                colors = if (ganadorID == state.jugador2ID)
                    androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                else
                    androidx.compose.material3.ButtonDefaults.buttonColors()
            ) {
                Text(
                    text = state.listaJugadores.find { it.JugadorId == state.jugador2ID }?.Nombres ?: "Jugador 2",
                    color = if (ganadorID == state.jugador2ID) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ganador: ${state.ganadorID?.let { id -> state.listaJugadores.find { it.JugadorId == id }?.Nombres } ?: "No determinado"}",
            style = MaterialTheme.typography.bodyLarge
        )

        // TODO Implementar checkbox de esFinalizada
        // TODO Implementar boton de Guardar
    }
}

@Preview
@Composable
fun EditPartidaBodyPreview() {
    val state = EditPartidaUiState(
        listaJugadores = listOf(
            Jugador(1, "Juan Pérez", 5),
            Jugador(2, "María López", 3)
        )
    )
    MaterialTheme {
        EditPartidaBody(state = state) { }
    }
}