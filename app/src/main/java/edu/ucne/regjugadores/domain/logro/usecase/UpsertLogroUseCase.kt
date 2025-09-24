package edu.ucne.regjugadores.domain.logro.usecase

import edu.ucne.regjugadores.domain.logro.model.Logro
import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import javax.inject.Inject

class UpsertLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {// todo Implementar validaciones
    suspend operator fun invoke(logro: Logro): Result<Int> {
        return try {
            val logroId = repository.upsert(logro)
            Result.success(logroId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}