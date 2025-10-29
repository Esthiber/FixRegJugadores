package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteJugadorUseCaseTest {

    private lateinit var useCase: DeleteJugadorUseCase
    private lateinit var repository: JugadorRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteJugadorUseCase(repository)
    }

    @Test
    fun `invoke con id elimina jugador exitosamente`() = runTest {
        // Given
        val jugadorId = "jugador-123"
        coEvery { repository.deleteJugadorById(jugadorId) } returns Resource.Success(Unit)

        // When
        val result = useCase(jugadorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { repository.deleteJugadorById(jugadorId) }
    }

    @Test
    fun `invoke con id retorna error cuando el jugador no existe`() = runTest {
        // Given
        val jugadorId = "non-existent"
        coEvery { repository.deleteJugadorById(jugadorId) } returns Resource.Error("No se encontro jugador con id $jugadorId")

        // When
        val result = useCase(jugadorId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("No se encontro jugador"))
    }

    @Test
    fun `invoke con id retorna error cuando jugador no tiene remoteId`() = runTest {
        // Given
        val jugadorId = "jugador-456"
        coEvery { repository.deleteJugadorById(jugadorId) } returns Resource.Error("Jugador no tiene remoteId")

        // When
        val result = useCase(jugadorId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Jugador no tiene remoteId", (result as Resource.Error).message)
    }

    @Test
    fun `invoke con objeto jugador elimina exitosamente`() = runTest {
        // Given
        val jugador = Jugador(
            id = "jugador-789",
            remoteId = 100,
            nombres = "Carlos Ruiz",
            email = "carlos@example.com"
        )
        coEvery { repository.deleteJugador(jugador) } returns Resource.Success(Unit)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { repository.deleteJugador(jugador) }
    }

    @Test
    fun `invoke con objeto jugador retorna error cuando no existe`() = runTest {
        // Given
        val jugador = Jugador(
            id = "non-existent",
            nombres = "Test User",
            email = "test@example.com"
        )
        coEvery { repository.deleteJugador(jugador) } returns Resource.Error("No se encontro jugador con id ${jugador.id}")

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("No se encontro jugador"))
    }

    @Test
    fun `invoke con objeto jugador retorna error cuando no tiene remoteId`() = runTest {
        // Given
        val jugador = Jugador(
            id = "jugador-local",
            remoteId = null,
            nombres = "Laura Sánchez",
            email = "laura@example.com"
        )
        coEvery { repository.deleteJugador(jugador) } returns Resource.Error("Jugador no tiene remoteId")

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Jugador no tiene remoteId", (result as Resource.Error).message)
    }

    @Test
    fun `invoke maneja error de red al eliminar`() = runTest {
        // Given
        val jugadorId = "jugador-999"
        coEvery { repository.deleteJugadorById(jugadorId) } returns Resource.Error("Error de red: Network unavailable")

        // When
        val result = useCase(jugadorId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Error de red"))
    }
}

