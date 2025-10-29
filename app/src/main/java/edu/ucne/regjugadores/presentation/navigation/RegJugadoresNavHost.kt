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
import edu.ucne.regjugadores.presentation.logro.LogrosScreen
import edu.ucne.regjugadores.presentation.logro.edit.EditLogroViewModel
import edu.ucne.regjugadores.presentation.logro.list.ListLogroViewModel
import edu.ucne.regjugadores.presentation.tictactoe.TicTacToeScreen
import kotlinx.coroutines.launch

@Composable
fun RegJugadoresNavHost(
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val editLogroViewModel: EditLogroViewModel = hiltViewModel()
    val listLogroViewModel: ListLogroViewModel = hiltViewModel()

    DrawerMenu(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.TicTacToe
        ) {
            composable<Screen.Jugadores> {
                JugadoresScreen()
            }

            composable<Screen.Partidas> {

            }

            composable<Screen.Logros> {
                LogrosScreen(
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    editLogroViewModel,
                    listLogroViewModel
                )
            }

            composable<Screen.TicTacToe> {
                TicTacToeScreen()
            }
        }
    }
}