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
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorScreen
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorUiEvent
import edu.ucne.regjugadores.presentation.jugador.edit.EditJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadorViewModel
import edu.ucne.regjugadores.presentation.jugador.list.ListJugadoresScreen
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
                val editPartidaViewModel: EditPartidaViewModel = hiltViewModel()
                val listPartidaViewModel: ListPartidaViewModel = hiltViewModel()

                val editViewModel: EditJugadorViewModel = hiltViewModel()
                val listViewModel: ListJugadorViewModel = hiltViewModel()
                val listState by listViewModel.state.collectAsState()

                LaunchedEffect(listState.navigateToEditId) {
                    listState.navigateToEditId?.let { id ->
                        editViewModel.onEvent(EditJugadorUiEvent.Load(id))
                        listViewModel.onNavigationHandled()
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ){
                        EditPartidaScreen(viewModel = editPartidaViewModel)
                        ListPartidaScreen(viewModel = listPartidaViewModel)
//                        EditJugadorScreen(viewModel = editViewModel)
//                        ListJugadoresScreen(viewModel = listViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RegJugadoresTheme {
        Greeting("Android")
    }
}