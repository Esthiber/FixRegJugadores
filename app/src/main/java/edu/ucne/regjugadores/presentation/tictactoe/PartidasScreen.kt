package edu.ucne.regjugadores.presentation.tictactoe

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.ucne.regjugadores.ui.theme.RegJugadoresTheme

@Composable
fun PartidasScreen(// TODO
//    viewModel: PartidasViewModel = hiltViewModel()
) {

}

@Composable
fun PartidasBody(
//    state: PartidasUiState,
//    onEvent: () -> Unit
) {
    Column {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text="Hello World"
        )
    }
}


@Preview
@Composable
fun PreviewPartidasScreen() {
    RegJugadoresTheme {
        PartidasBody(

        )
    }
}


