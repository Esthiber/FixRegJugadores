package edu.ucne.regjugadores.presentation.partida.edit

import androidx.compose.foundation.BorderStroke
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucne.regjugadores.presentation.navigation.Screen

@Composable
fun EditPartidaScreen(
    viewModel: EditPartidaViewModel = hiltViewModel(),
    onCancel: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    navController: NavController? = null
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) {
            navController?.navigate(Screen.TicTacToe)
            onSaveSuccess()
        }
    }

    EditPartidaBody(
        state = state,
        onEvent = { event ->
            when (event) {
                EditPartidaUiEvent.Cancel -> onCancel()
                else -> viewModel.onEvent(event)
            }
        }
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
            .fillMaxWidth()
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

        // Jugador 1 Selection
        var jugador1Expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = jugador1Expanded,
            onExpandedChange = { jugador1Expanded = it }
        ) {
            OutlinedTextField(
                value = state.listaJugadores.find { it.JugadorId == state.jugador1ID }?.Nombres
                    ?: "Seleccione Jugador 1",
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

        // Jugador 2 Selection
        var jugador2Expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = jugador2Expanded,
            onExpandedChange = { jugador2Expanded = it }
        ) {
            OutlinedTextField(
                value = state.listaJugadores.find { it.JugadorId == state.jugador2ID }?.Nombres
                    ?: "Seleccione Jugador 2",
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

        Spacer(modifier = Modifier.height(24.dp))

        val jugador1Seleccionado = state.jugador1ID != 0
        val jugador2Seleccionado = state.jugador2ID != 0
        val ganadorSeleccionable = jugador1Seleccionado && jugador2Seleccionado
        val ganadorID = state.ganadorID

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Selecciona el ganador:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = { onEvent(EditPartidaUiEvent.GanadorIDChanged(state.jugador1ID)) },
                        enabled = ganadorSeleccionable,
                        modifier = Modifier.weight(1f),
                        border = if (ganadorID == state.jugador1ID)
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        else null,
                        colors = if (ganadorID == state.jugador1ID)
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        else
                            ButtonDefaults.buttonColors()
                    ) {
                        Text(
                            text = state.listaJugadores.find { it.JugadorId == state.jugador1ID }?.Nombres
                                ?: "Jugador 1",
                            color = if (ganadorID == state.jugador1ID) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Button(
                        onClick = { onEvent(EditPartidaUiEvent.GanadorIDChanged(state.jugador2ID)) },
                        enabled = ganadorSeleccionable,
                        modifier = Modifier.weight(1f),
                        border = if (ganadorID == state.jugador2ID)
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        else null,
                        colors = if (ganadorID == state.jugador2ID)
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        else
                            ButtonDefaults.buttonColors()
                    ) {
                        Text(
                            text = state.listaJugadores.find { it.JugadorId == state.jugador2ID }?.Nombres
                                ?: "Jugador 2",
                            color = if (ganadorID == state.jugador2ID) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                if (!ganadorSeleccionable) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Seleccione ambos jugadores para elegir un ganador",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (ganadorID != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ganador: ${state.listaJugadores.find { it.JugadorId == ganadorID }?.Nombres}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox para especificar si la partida es finalizada
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.esFinalizada,
                    onCheckedChange = { checked ->
                        onEvent(
                            EditPartidaUiEvent.EsFinalizadaChanged(
                                checked
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¿Partida finalizada?",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = { onEvent(EditPartidaUiEvent.Save) },
                enabled = !state.jugadoresLoading && jugador1Seleccionado && jugador2Seleccionado && ganadorID != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar partida")
            }
            OutlinedButton(
                onClick = { onEvent(EditPartidaUiEvent.Cancel) },
                enabled = !state.jugadoresLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }

        if (state.isSaving) {
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
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