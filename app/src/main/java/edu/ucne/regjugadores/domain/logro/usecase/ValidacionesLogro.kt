package edu.ucne.regjugadores.domain.logro.usecase

data class ValidacionesLogro(
    val isValid:Boolean,
    val error:String? = null
)

fun ValidarTitulo(titulo:String): ValidacionesLogro{
    if(titulo.isBlank()){
        return ValidacionesLogro(
            isValid = false,
            error = "El titulo no puede estar vacio"
        )
    }
    if(titulo.length < 3){
        return ValidacionesLogro(
            isValid = false,
            error = "El titulo debe tener al menos 3 caracteres"
        )
    }
    if(titulo.length > 50){
        return ValidacionesLogro(
            isValid = false,
            error = "El titulo no puede tener mas de 50 caracteres"
        )
    }
    return ValidacionesLogro(isValid = true)
}

fun ValidarDescripcion(descripcion:String): ValidacionesLogro{

    if(descripcion.length > 200){
        return ValidacionesLogro(
            isValid = false,
            error = "La descripcion no puede tener mas de 200 caracteres"
        )
    }
    return ValidacionesLogro(isValid = true)
}