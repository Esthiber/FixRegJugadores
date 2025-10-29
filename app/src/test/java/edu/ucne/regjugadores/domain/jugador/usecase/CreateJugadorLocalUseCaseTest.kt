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
class CreateJugadorLocalUseCaseTest {

    private lateinit var useCase: CreateJugadorLocalUseCase
    private lateinit var repository: JugadorRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = CreateJugadorLocalUseCase(repository)
    }

    @Test
    fun `invoke llama al repositorio correctamente`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "Juan Pérez",
            email = "juan@example.com"
        )
        coEvery { repository.createJugadorLocal(jugador) } returns Resource.Success(jugador)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(jugador, (result as Resource.Success).data)
        coVerify { repository.createJugadorLocal(jugador) }
    }

    @Test
    fun `invoke propaga errores del repositorio`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "María García",
            email = "maria@example.com"
        )
        val errorMessage = "Database error"
        coEvery { repository.createJugadorLocal(jugador) } returns Resource.Error(errorMessage)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }

    @Test
    fun `invoke crea jugador con isPendingCreate true`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "Pedro López",
            email = "pedro@example.com",
            isPendingCreate = false
        )
        val jugadorPendiente = jugador.copy(isPendingCreate = true)
        coEvery { repository.createJugadorLocal(jugador) } returns Resource.Success(jugadorPendiente)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data?.isPendingCreate == true)
        coVerify { repository.createJugadorLocal(jugador) }
    }

    @Test
    fun `invoke maneja jugador con datos minimos`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "Ana",
            email = "a@b.com"
        )
        coEvery { repository.createJugadorLocal(jugador) } returns Resource.Success(jugador)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Ana", (result as Resource.Success).data?.nombres)
        assertEquals("a@b.com", result.data?.email)
    }
}

