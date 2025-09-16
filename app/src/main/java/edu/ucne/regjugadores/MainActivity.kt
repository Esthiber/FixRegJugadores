package edu.ucne.regjugadores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorScreen
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorUiEvent
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadoresScreen
import edu.ucne.regjugadores.presentation.navigation.RegJugadoresNavHost
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaScreen
import edu.ucne.regjugadores.presentation.partida.edit.EditPartidaViewModel
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaScreen
import edu.ucne.regjugadores.presentation.partida.list.ListPartidaViewModel
import edu.ucne.regjugadores.ui.theme.RegJugadoresTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegJugadoresTheme {
                val navHost = rememberNavController()
                RegJugadoresNavHost(navHost)
            }

//            RegJugadoresTheme {
//                val editPartidaViewModel: EditPartidaViewModel = hiltViewModel()
//                val listPartidaViewModel: ListPartidaViewModel = hiltViewModel()
//
//                val editJugadorViewModel: EditJugadorViewModel = hiltViewModel()
//                val listJugadorViewModel: ListJugadorViewModel = hiltViewModel()
//                val listState by listJugadorViewModel.state.collectAsState()
//
//                LaunchedEffect(listState.navigateToEditId) {
//                    listState.navigateToEditId?.let { id ->
//                        editJugadorViewModel.onEvent(EditJugadorUiEvent.Load(id))
//                        listJugadorViewModel.onNavigationHandled()
//                    }
//                }
//
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding)
//                    ){
//                        EditPartidaScreen(viewModel = editPartidaViewModel)
//                        ListPartidaScreen(viewModel = listPartidaViewModel)
////                        EditJugadorScreen(viewModel = editJugadorViewModel)
////                        ListJugadoresScreen(viewModel = listJugadorViewModel)
//                    }
//                }
//            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    RegJugadoresTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                MainActivity()
            }
        }
    }
}