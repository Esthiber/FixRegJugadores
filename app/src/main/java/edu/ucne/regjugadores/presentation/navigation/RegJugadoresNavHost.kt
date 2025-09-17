package edu.ucne.regjugadores.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.regjugadores.presentation.jugador.JugadoresScreen
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadorViewModel
import edu.ucne.regjugadores.presentation.partida.PartidaScreen
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaViewModel
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaViewModel
import kotlinx.coroutines.launch

@Composable
fun RegJugadoresNavHost(
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val editJugadorViewModel: EditJugadorViewModel = hiltViewModel()
    val listJugadorViewModel: ListJugadorViewModel = hiltViewModel()

    val editPartidaViewModel: EditPartidaViewModel = hiltViewModel()
    val listPartidaViewModel: ListPartidaViewModel = hiltViewModel()

    DrawerMenu(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.Jugadores
        ) {
            composable<Screen.Jugadores> {
                JugadoresScreen(
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    editJugadorViewModel,
                    listJugadorViewModel
                )
            }
            composable<Screen.Partidas> {
                PartidaScreen(
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    editPartidaViewModel,
                    listPartidaViewModel
                )
            }
        }
    }
}