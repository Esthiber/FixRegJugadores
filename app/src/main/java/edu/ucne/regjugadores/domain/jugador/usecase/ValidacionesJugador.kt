package edu.ucne.regjugadores.domain.jugador.usecase

data class ValidacionesJugador(
    val isValid:Boolean,
    val error:String? = null
)
fun validateNombres(value:String): ValidacionesJugador{
    if(value.isBlank())
        return ValidacionesJugador(false, "El nombre no puede estar vacio")
    if(value.length < 3)
        return ValidacionesJugador(false, "El nombre debe tener al menos 3 caracteres")
    if(value.length > 50)
        return ValidacionesJugador(false, "El nombre no puede tener mas de 50 caracteres")

    return ValidacionesJugador(true)
}

fun validatePartidas(value: String): ValidacionesJugador {
    if(value.isBlank())
        return ValidacionesJugador(false, "El campo partidas no puede estar vacio")

    val partidas = value.toIntOrNull()
    if(partidas == null || partidas < 0)
        return ValidacionesJugador(false, "El campo partidas debe ser un numero valido mayor o igual a 0")

    return ValidacionesJugador(true)
}
