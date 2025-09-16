package edu.ucne.regjugadores.presentation.partida

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.regjugadores.presentation.components.TopBarComponent
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaScreen
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaViewModel
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaScreen
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaViewModel
import edu.ucne.regjugadores.ui.theme.RegJugadoresTheme

@Composable
fun PartidaScreen(
    onDrawer: () -> Unit = {},
    editViewModel: EditPartidaViewModel = hiltViewModel(),
    listViewModel: ListPartidaViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Registro de Partidas",
                onDrawer
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            PartidaScreenBody(editViewModel, listViewModel)
        }
    }
}

@Composable
fun PartidaScreenBody(
    edit: EditPartidaViewModel = hiltViewModel(),
    list: ListPartidaViewModel = hiltViewModel()
) {
    EditPartidaScreen(edit)
    ListPartidaScreen(list)
}

@Preview
@Composable
fun PartidaScreenPreview() {
    RegJugadoresTheme {
        PartidaScreenBody()
    }
}