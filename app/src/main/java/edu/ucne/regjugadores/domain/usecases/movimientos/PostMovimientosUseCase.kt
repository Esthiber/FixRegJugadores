package edu.ucne.regjugadores.domain.usecases.movimientos

import edu.ucne.regjugadores.domain.model.movimiento
import edu.ucne.regjugadores.domain.repository.MovimientosRepository
import javax.inject.Inject

class PostMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
){
    suspend operator fun invoke(movimiento: movimiento) =
        repository.postMovimiento(movimiento)
}