package edu.ucne.regjugadores.presentation.jugador

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.regjugadores.presentation.components.TopBarComponent
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorScreen
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadoresScreen
import edu.ucne.regjugadores.ui.theme.RegJugadoresTheme

@Composable
fun JugadoresScreen(
    onDrawer: () -> Unit = {},
    editJugadorViewModel: EditJugadorViewModel = hiltViewModel(),
    listJugadorViewModel: ListJugadorViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Registro de Jugadores",
                onDrawer
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            JugadoresScreenBody(editJugadorViewModel, listJugadorViewModel)
        }
    }
}

@Composable
fun JugadoresScreenBody(
    edit: EditJugadorViewModel,
    list: ListJugadorViewModel
) {
    EditJugadorScreen(edit)
    ListJugadoresScreen(list)
}

@Preview
@Composable
fun JugadoresScreenPreview() {
    RegJugadoresTheme {
        JugadoresScreenBody(hiltViewModel(), hiltViewModel())
    }
}