package edu.ucne.regjugadores.presentation.logro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.regjugadores.presentation.components.TopBarComponent
import edu.ucne.regjugadores.presentation.logro.edit.EditLogroScreen
import edu.ucne.regjugadores.presentation.logro.edit.EditLogroViewModel
import edu.ucne.regjugadores.presentation.logro.list.ListLogroViewModel
import edu.ucne.regjugadores.presentation.logro.list.ListLogrosScreen
import edu.ucne.regjugadores.ui.theme.RegJugadoresTheme

@Composable
fun LogrosScreen(
    onDrawer:()->Unit = {},
    editViewModel: EditLogroViewModel = hiltViewModel(),
    listViewModel: ListLogroViewModel = hiltViewModel()
){
    Scaffold (
        topBar = {
            TopBarComponent(
                title = "Registro de Logros",
                onDrawer
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ){
            LogroScreenBody(editViewModel, listViewModel)
        }

    }
}

@Composable
fun LogroScreenBody(
    edit: EditLogroViewModel,
    list: ListLogroViewModel
){
    EditLogroScreen(edit)
    ListLogrosScreen(list)
}

@Preview
@Composable
fun LogroScreenPreview(){
    RegJugadoresTheme {
        LogroScreenBody(hiltViewModel(), hiltViewModel())
    }
}