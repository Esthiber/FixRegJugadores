package edu.ucne.regjugadores.domain.logro.usecase

import edu.ucne.regjugadores.domain.logro.model.Logro
import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import javax.inject.Inject

class GetLogroByIdUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(id: Int?): Logro? = repository.getLogroById(id)
}