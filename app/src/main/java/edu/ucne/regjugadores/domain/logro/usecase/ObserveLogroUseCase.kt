package edu.ucne.regjugadores.domain.logro.usecase

import edu.ucne.regjugadores.domain.logro.model.Logro
import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    operator fun invoke(): Flow<List<Logro>> = repository.observeLogros()
}