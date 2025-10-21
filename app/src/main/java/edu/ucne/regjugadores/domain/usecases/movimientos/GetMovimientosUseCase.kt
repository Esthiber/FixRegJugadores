package edu.ucne.regjugadores.domain.usecases.movimientos

import edu.ucne.regjugadores.domain.repository.MovimientosRepository
import javax.inject.Inject

class GetMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
) {
    operator fun invoke(partidaId: Int) =
        repository.getMovimientos(partidaId)
}