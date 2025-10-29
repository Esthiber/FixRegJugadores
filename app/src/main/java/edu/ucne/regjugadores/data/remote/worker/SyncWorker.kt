package edu.ucne.regjugadores.data.remote.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val jugadorRepository: JugadorRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return when (jugadorRepository.postPendingJugadores()) {
            is Resource.Success -> Result.success()
            is Resource.Error -> Result.retry()
            else -> Result.failure()
        }
    }
}