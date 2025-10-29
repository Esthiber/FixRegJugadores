package edu.ucne.regjugadores.presentation.jugador

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.usecase.CreateJugadorLocalUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.DeleteJugadorUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.ObserveJugadorUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.TriggerSyncUseCase
import edu.ucne.regjugadores.domain.jugador.usecase.UpsertJugadorUseCase
import edu.ucne.regjugadores.utils.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class JugadorViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: JugadorViewModel
    private lateinit var observeJugadorUseCase: ObserveJugadorUseCase
    private lateinit var createJugadorLocalUseCase: CreateJugadorLocalUseCase
    private lateinit var upsertJugadorUseCase: UpsertJugadorUseCase
    private lateinit var deleteJugadorUseCase: DeleteJugadorUseCase
    private lateinit var triggerSyncUseCase: TriggerSyncUseCase

    @Before
    fun setup() {
        observeJugadorUseCase = mockk()
        createJugadorLocalUseCase = mockk()
        upsertJugadorUseCase = mockk()
        deleteJugadorUseCase = mockk()
        triggerSyncUseCase = mockk(relaxed = true)

        every { observeJugadorUseCase() } returns flowOf(emptyList())

        viewModel = JugadorViewModel(
            observeJugadorUseCase,
            createJugadorLocalUseCase,
            upsertJugadorUseCase,
            deleteJugadorUseCase,
            triggerSyncUseCase
        )
    }

    @Test
    fun `onEvent ShowCreateSheet actualiza estado correctamente`() = runTest {
        // When
        viewModel.onEvent(JugadorEvent.ShowCreateSheet)

        // Then
        assertTrue(viewModel.state.value.showCreateSheet)
    }

    @Test
    fun `onEvent HideCreateSheet limpia nombre y email y cierra sheet`() = runTest {
        // Given
        viewModel.onEvent(JugadorEvent.ShowCreateSheet)
        viewModel.onEvent(JugadorEvent.OnNombreChange("Juan Pérez"))
        viewModel.onEvent(JugadorEvent.OnEmailChange("juan@example.com"))

        // When
        viewModel.onEvent(JugadorEvent.HideCreateSheet)

        // Then
        assertFalse(viewModel.state.value.showCreateSheet)
        assertEquals("", viewModel.state.value.jugadorNombre)
        assertEquals("", viewModel.state.value.jugadorEmail)
    }

    @Test
    fun `onEvent OnNombreChange actualiza nombre en estado`() = runTest {
        // When
        viewModel.onEvent(JugadorEvent.OnNombreChange("María García"))

        // Then
        assertEquals("María García", viewModel.state.value.jugadorNombre)
    }

    @Test
    fun `onEvent OnEmailChange actualiza email en estado`() = runTest {
        // When
        viewModel.onEvent(JugadorEvent.OnEmailChange("maria@example.com"))

        // Then
        assertEquals("maria@example.com", viewModel.state.value.jugadorEmail)
    }

    @Test
    fun `onEvent CrearJugador crea jugador y cierra sheet cuando es exitoso`() = runTest {
        // Given
        val nombre = "Pedro López"
        val email = "pedro@example.com"
        val jugador = Jugador(nombres = nombre, email = email)
        coEvery { createJugadorLocalUseCase(any()) } returns Resource.Success(jugador)
        every { triggerSyncUseCase() } just Runs

        // When
        viewModel.onEvent(JugadorEvent.CrearJugador(nombre, email))
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.showCreateSheet)
        assertEquals("", viewModel.state.value.jugadorNombre)
        assertEquals("", viewModel.state.value.jugadorEmail)
        assertEquals("Jugador creado localmente", viewModel.state.value.userMessage)
        coVerify { createJugadorLocalUseCase(any()) }
        verify { triggerSyncUseCase() }
    }

    @Test
    fun `onEvent CrearJugador muestra error cuando falla`() = runTest {
        // Given
        val nombre = "Ana Martínez"
        val email = "ana@example.com"
        val errorMessage = "Error de base de datos"
        coEvery { createJugadorLocalUseCase(any()) } returns Resource.Error(errorMessage)

        // When
        viewModel.onEvent(JugadorEvent.CrearJugador(nombre, email))
        advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.state.value.userMessage)
    }

    @Test
    fun `onEvent CrearJugador no cierra sheet cuando falla`() = runTest {
        // Given
        viewModel.onEvent(JugadorEvent.ShowCreateSheet)
        val nombre = "Test User"
        val email = "test@example.com"
        coEvery { createJugadorLocalUseCase(any()) } returns Resource.Error("Error")

        // When
        viewModel.onEvent(JugadorEvent.CrearJugador(nombre, email))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value.showCreateSheet)
    }

    @Test
    fun `onEvent UpdateJugador actualiza jugador correctamente`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = 100,
            nombres = "Carlos Ruiz",
            email = "carlos@example.com"
        )
        coEvery { upsertJugadorUseCase(jugador) } returns Resource.Success(Unit)

        // When
        viewModel.onEvent(JugadorEvent.UpdateJugador(jugador))
        advanceUntilIdle()

        // Then
        assertEquals("Jugador actualizado", viewModel.state.value.userMessage)
        coVerify { upsertJugadorUseCase(jugador) }
    }

    @Test
    fun `onEvent UpdateJugador muestra error cuando falla`() = runTest {
        // Given
        val jugador = Jugador(nombres = "Test", email = "test@example.com")
        val errorMessage = "No remoteId"
        coEvery { upsertJugadorUseCase(jugador) } returns Resource.Error(errorMessage)

        // When
        viewModel.onEvent(JugadorEvent.UpdateJugador(jugador))
        advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.state.value.userMessage)
    }

    @Test
    fun `onEvent DeleteJugador elimina jugador correctamente`() = runTest {
        // Given
        val jugadorId = "jugador-123"
        coEvery { deleteJugadorUseCase(jugadorId) } returns Resource.Success(Unit)

        // When
        viewModel.onEvent(JugadorEvent.DeleteJugador(jugadorId))
        advanceUntilIdle()

        // Then
        assertEquals("Jugador eliminado", viewModel.state.value.userMessage)
        coVerify { deleteJugadorUseCase(jugadorId) }
    }

    @Test
    fun `onEvent DeleteJugador muestra error cuando falla`() = runTest {
        // Given
        val jugadorId = "jugador-999"
        val errorMessage = "Jugador no tiene remoteId"
        coEvery { deleteJugadorUseCase(jugadorId) } returns Resource.Error(errorMessage)

        // When
        viewModel.onEvent(JugadorEvent.DeleteJugador(jugadorId))
        advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.state.value.userMessage)
    }

    @Test
    fun `onEvent UserMessageShown limpia mensaje`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = 100,
            nombres = "Test",
            email = "test@example.com"
        )
        coEvery { upsertJugadorUseCase(jugador) } returns Resource.Success(Unit)
        viewModel.onEvent(JugadorEvent.UpdateJugador(jugador))
        advanceUntilIdle()

        // When
        viewModel.onEvent(JugadorEvent.UserMessageShown)

        // Then
        assertNull(viewModel.state.value.userMessage)
    }

    @Test
    fun `estado inicial tiene jugadores vacios`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value.jugadores.isEmpty())
        assertFalse(viewModel.state.value.showCreateSheet)
        assertEquals("", viewModel.state.value.jugadorNombre)
        assertEquals("", viewModel.state.value.jugadorEmail)
    }

    @Test
    fun `observeJugadores actualiza lista cuando hay cambios`() = runTest {
        // Given
        val jugadores = listOf(
            Jugador(id = "1", nombres = "Juan", email = "juan@example.com"),
            Jugador(id = "2", nombres = "María", email = "maria@example.com")
        )
        every { observeJugadorUseCase() } returns flowOf(jugadores)

        // When - Crear nuevo ViewModel para que se ejecute el init
        val newViewModel = JugadorViewModel(
            observeJugadorUseCase,
            createJugadorLocalUseCase,
            upsertJugadorUseCase,
            deleteJugadorUseCase,
            triggerSyncUseCase
        )
        advanceUntilIdle()

        // Then
        assertEquals(2, newViewModel.state.value.jugadores.size)
        assertFalse(newViewModel.state.value.isLoading)
        assertEquals("Juan", newViewModel.state.value.jugadores[0].nombres)
        assertEquals("María", newViewModel.state.value.jugadores[1].nombres)
    }

    @Test
    fun `observeJugadores cambia isLoading a false cuando termina`() = runTest {
        // Given
        every { observeJugadorUseCase() } returns flowOf(emptyList())

        // When
        val newViewModel = JugadorViewModel(
            observeJugadorUseCase,
            createJugadorLocalUseCase,
            upsertJugadorUseCase,
            deleteJugadorUseCase,
            triggerSyncUseCase
        )
        advanceUntilIdle()

        // Then
        assertFalse(newViewModel.state.value.isLoading)
    }

    @Test
    fun `crear jugador con nombre y email vacios llama al use case`() = runTest {
        // Given
        coEvery { createJugadorLocalUseCase(any()) } returns Resource.Success(
            Jugador(nombres = "", email = "")
        )
        every { triggerSyncUseCase() } just Runs

        // When
        viewModel.onEvent(JugadorEvent.CrearJugador("", ""))
        advanceUntilIdle()

        // Then
        coVerify { createJugadorLocalUseCase(any()) }
    }

    @Test
    fun `multiples cambios de nombre mantienen el ultimo valor`() = runTest {
        // When
        viewModel.onEvent(JugadorEvent.OnNombreChange("Primer nombre"))
        viewModel.onEvent(JugadorEvent.OnNombreChange("Segundo nombre"))
        viewModel.onEvent(JugadorEvent.OnNombreChange("Tercer nombre"))

        // Then
        assertEquals("Tercer nombre", viewModel.state.value.jugadorNombre)
    }

    @Test
    fun `multiples cambios de email mantienen el ultimo valor`() = runTest {
        // When
        viewModel.onEvent(JugadorEvent.OnEmailChange("primero@example.com"))
        viewModel.onEvent(JugadorEvent.OnEmailChange("segundo@example.com"))
        viewModel.onEvent(JugadorEvent.OnEmailChange("tercero@example.com"))

        // Then
        assertEquals("tercero@example.com", viewModel.state.value.jugadorEmail)
    }

    @Test
    fun `crear jugador exitosamente dispara sincronizacion`() = runTest {
        // Given
        val jugador = Jugador(nombres = "Test", email = "test@example.com")
        coEvery { createJugadorLocalUseCase(any()) } returns Resource.Success(jugador)
        every { triggerSyncUseCase() } just Runs

        // When
        viewModel.onEvent(JugadorEvent.CrearJugador("Test", "test@example.com"))
        advanceUntilIdle()

        // Then
        verify(exactly = 1) { triggerSyncUseCase() }
    }

    @Test
    fun `crear jugador fallido no dispara sincronizacion`() = runTest {
        // Given
        coEvery { createJugadorLocalUseCase(any()) } returns Resource.Error("Error")

        // When
        viewModel.onEvent(JugadorEvent.CrearJugador("Test", "test@example.com"))
        advanceUntilIdle()

        // Then
        verify(exactly = 0) { triggerSyncUseCase() }
    }
}

