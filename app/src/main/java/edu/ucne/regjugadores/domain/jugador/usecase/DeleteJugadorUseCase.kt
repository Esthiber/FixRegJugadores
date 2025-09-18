package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
    private val canDeleteJugadorUseCase: CanDeleteJugadorUseCase
) {
    sealed class DeleteResult {
        object Success : DeleteResult()
        object HasPartidas : DeleteResult()
        data class Error(val message: String) : DeleteResult()
    }

    suspend operator fun invoke(id: Int): DeleteResult {
        return try {
            if (!canDeleteJugadorUseCase(id)) {
                DeleteResult.HasPartidas
            } else {
                repository.deleteJugadorbyId(id)
                DeleteResult.Success
            }
        } catch (e: Exception) {
            DeleteResult.Error(e.message ?: "Error desconocido al eliminar jugador")
        }
    }
}