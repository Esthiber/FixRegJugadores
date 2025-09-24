package edu.ucne.regjugadores.domain.logro.usecase

import edu.ucne.regjugadores.domain.logro.repository.LogroRepository
import javax.inject.Inject

class DeleteLogroUseCase @Inject constructor(
    private val repository: LogroRepository,
) {
    sealed class DeleteResult {
        object Success : DeleteResult()
        data class Error(val message: String) : DeleteResult()
    }

    suspend operator fun invoke(id: Int): DeleteResult {
        return try {
            repository.deleteLogroById(id)
            DeleteResult.Success
        } catch (e: Exception) {
            DeleteResult.Error(e.message ?: "Error desconocido al eliminar logro")
        }
    }
}