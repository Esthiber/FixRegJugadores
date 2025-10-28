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

fun validateEmail(value:String): ValidacionesJugador{
    if(value.isBlank())
        return ValidacionesJugador(false, "El email no puede estar vacio")
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.+)(\\.)(.+)"
    if(!value.matches(emailRegex.toRegex()))
        return ValidacionesJugador(false, "El email no es valido")

    return ValidacionesJugador(true)
}
