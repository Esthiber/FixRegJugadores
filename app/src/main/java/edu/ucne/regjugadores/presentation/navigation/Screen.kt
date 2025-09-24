package edu.ucne.regjugadores.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen
{
    @Serializable
    data object Jugadores:Screen()

    @Serializable
    data object Partidas:Screen()

    @Serializable
    data object Logros:Screen()

    @Serializable
    data object TicTacToe:Screen()

}