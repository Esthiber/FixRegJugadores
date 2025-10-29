package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveJugadorUseCaseTest {

    private lateinit var useCase: ObserveJugadorUseCase
    private lateinit var repository: JugadorRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveJugadorUseCase(repository)
    }

    @Test
    fun `invoke retorna flow con lista de jugadores`() = runTest {
        // Given
        val jugadores = listOf(
            Jugador(
                id = "1",
                remoteId = 100,
                nombres = "Juan Pérez",
                email = "juan@example.com"
            ),
            Jugador(
                id = "2",
                remoteId = 200,
                nombres = "María García",
                email = "maria@example.com"
            )
        )
        every { repository.observeJugador() } returns flowOf(jugadores)

        // When
        val result = useCase().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Juan Pérez", result[0].nombres)
        assertEquals("María García", result[1].nombres)
        verify { repository.observeJugador() }
    }

    @Test
    fun `invoke retorna flow con lista vacia cuando no hay jugadores`() = runTest {
        // Given
        every { repository.observeJugador() } returns flowOf(emptyList())

        // When
        val result = useCase().first()

        // Then
        assertTrue(result.isEmpty())
        verify { repository.observeJugador() }
    }

    @Test
    fun `invoke retorna flow con un solo jugador`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = 100,
            nombres = "Pedro López",
            email = "pedro@example.com"
        )
        every { repository.observeJugador() } returns flowOf(listOf(jugador))

        // When
        val result = useCase().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Pedro López", result[0].nombres)
        assertEquals("pedro@example.com", result[0].email)
    }

    @Test
    fun `invoke retorna flow con jugadores ordenados`() = runTest {
        // Given
        val jugadores = listOf(
            Jugador(id = "3", nombres = "Carlos", email = "carlos@example.com"),
            Jugador(id = "2", nombres = "Beatriz", email = "beatriz@example.com"),
            Jugador(id = "1", nombres = "Ana", email = "ana@example.com")
        )
        every { repository.observeJugador() } returns flowOf(jugadores)

        // When
        val result = useCase().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("3", result[0].id)
        assertEquals("2", result[1].id)
        assertEquals("1", result[2].id)
    }

    @Test
    fun `invoke retorna flow que incluye jugadores pendientes de sincronizar`() = runTest {
        // Given
        val jugadores = listOf(
            Jugador(
                id = "1",
                remoteId = 100,
                nombres = "Juan Pérez",
                email = "juan@example.com",
                isPendingCreate = false
            ),
            Jugador(
                id = "2",
                remoteId = null,
                nombres = "María García",
                email = "maria@example.com",
                isPendingCreate = true
            )
        )
        every { repository.observeJugador() } returns flowOf(jugadores)

        // When
        val result = useCase().first()

        // Then
        assertEquals(2, result.size)
        assertEquals(false, result[0].isPendingCreate)
        assertEquals(true, result[1].isPendingCreate)
    }
}

