package edu.ucne.regjugadores.domain.logro.usecase

import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import javax.inject.Inject

class LogroExisteUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(titulo: String,idActual: Int?): Boolean {
        return repository.logroExisteByTitulo(titulo,idActual)
    }
}