package edu.ucne.regjugadores.domain.partida.usecase

data class ValidacionesPartidas(
    val isValid: Boolean,
    val error:String? = null
)

fun validateJugador1(value:String, jugador2Id: String): ValidacionesPartidas{
    if(value.isBlank())
        return ValidacionesPartidas(false, "Debe de haber un jugador 1.")
    val jugador1 = value.toIntOrNull()
    val jugador2 = jugador2Id.toIntOrNull()

    if(jugador1 != null && jugador2 != null){
        if(jugador1 == jugador2)
            return ValidacionesPartidas(false, "Los jugadores no pueden ser el mismo.")
    }

    if(jugador1 == null || jugador1 <= 0)
        return ValidacionesPartidas(false, "El jugador 1 debe de ser valido.")
    return ValidacionesPartidas(true)
}

fun validateJugador2(value:String, jugador1Id:String): ValidacionesPartidas{
    if(value.isBlank())
        return ValidacionesPartidas(false, "Debe de haber un jugador 2.")

    val jugador2 = value.toIntOrNull()
    val jugador1 = jugador1Id.toIntOrNull()

    if(jugador1 != null && jugador2 != null){
        if(jugador1 == jugador2)
            return ValidacionesPartidas(false, "Los jugadores no pueden ser el mismo.")
    }

    if(jugador2 == null || jugador2 <= 0)
        return ValidacionesPartidas(false, "El jugador 2 debe de ser valido.")
    return ValidacionesPartidas(true)
}