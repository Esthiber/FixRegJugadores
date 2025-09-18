package edu.ucne.regjugadores.presentation.partida

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucne.regjugadores.presentation.components.TopBarComponent
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaScreen
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaUiEvent
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaViewModel
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaScreen
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaViewModel
import edu.ucne.regjugadores.ui.theme.RegJugadoresTheme

@Composable
fun PartidaScreen(
    onDrawer: () -> Unit = {},
    editViewModel: EditPartidaViewModel = hiltViewModel(),
    listViewModel: ListPartidaViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    var showEdit by remember { mutableStateOf(false) }
    var partidaIdToEdit by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopBarComponent(
                title = if (showEdit) {
                    if (partidaIdToEdit != null) "Editar Partida" else "Nueva Partida"
                } else "Registro de Partidas",
                onDrawer
            )
        },
        floatingActionButton = {
            if (!showEdit) {
                FloatingActionButton(
                    onClick = {
                        editViewModel.onEvent(EditPartidaUiEvent.Cancel)
                        editViewModel.onEvent(EditPartidaUiEvent.Load(null))
                        partidaIdToEdit = null
                        showEdit = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Partida")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (showEdit) {
                EditPartidaScreen(
                    viewModel = editViewModel,
                    onCancel = {
                        showEdit = false
                        partidaIdToEdit = null
                        editViewModel.onEvent(EditPartidaUiEvent.Cancel)
                    },
                    onSaveSuccess = {
                        showEdit = false
                        partidaIdToEdit = null
                        listViewModel.onEvent(edu.ucne.regjugadores.presentation.partida.list.ListPartidaUiEvent.Load)
                    },
                    navController = navController
                )
            } else {
                ListPartidaScreen(
                    viewModel = listViewModel,
                    onEditPartida = { partidaId ->
                        editViewModel.onEvent(EditPartidaUiEvent.Cancel)
                        editViewModel.onEvent(EditPartidaUiEvent.Load(partidaId))
                        partidaIdToEdit = partidaId
                        showEdit = true
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PartidaScreenPreview() {
    RegJugadoresTheme {
        PartidaScreen()
    }
}