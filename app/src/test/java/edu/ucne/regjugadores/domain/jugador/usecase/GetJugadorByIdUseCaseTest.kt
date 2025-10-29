package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetJugadorByIdUseCaseTest {

    private lateinit var useCase: GetJugadorByIdUseCase
    private lateinit var repository: JugadorRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetJugadorByIdUseCase(repository)
    }

    @Test
    fun `invoke retorna jugador cuando existe`() = runTest {
        // Given
        val jugadorId = 100
        val jugador = Jugador(
            id = "1",
            remoteId = jugadorId,
            nombres = "Juan Pérez",
            email = "juan@example.com"
        )
        coEvery { repository.getJugadorById(jugadorId) } returns jugador

        // When
        val result = useCase(jugadorId)

        // Then
        assertEquals(jugador, result)
        assertEquals("Juan Pérez", result?.nombres)
        coVerify { repository.getJugadorById(jugadorId) }
    }

    @Test
    fun `invoke retorna null cuando jugador no existe`() = runTest {
        // Given
        val jugadorId = 999
        coEvery { repository.getJugadorById(jugadorId) } returns null

        // When
        val result = useCase(jugadorId)

        // Then
        assertNull(result)
        coVerify { repository.getJugadorById(jugadorId) }
    }

    @Test
    fun `invoke maneja id null`() = runTest {
        // Given
        coEvery { repository.getJugadorById(null) } returns null

        // When
        val result = useCase(null)

        // Then
        assertNull(result)
        coVerify { repository.getJugadorById(null) }
    }

    @Test
    fun `invoke retorna jugador con todos los campos completos`() = runTest {
        // Given
        val jugadorId = 50
        val jugador = Jugador(
            id = "abc-123",
            remoteId = jugadorId,
            nombres = "María García López",
            email = "maria.garcia@example.com",
            isPendingCreate = false
        )
        coEvery { repository.getJugadorById(jugadorId) } returns jugador

        // When
        val result = useCase(jugadorId)

        // Then
        assertEquals(jugador, result)
        assertEquals("abc-123", result?.id)
        assertEquals(50, result?.remoteId)
        assertEquals("María García López", result?.nombres)
        assertEquals("maria.garcia@example.com", result?.email)
        assertEquals(false, result?.isPendingCreate)
    }
}

