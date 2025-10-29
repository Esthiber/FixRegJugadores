package edu.ucne.regjugadores.data.jugadores.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.regjugadores.data.jugadores.local.JugadorDao
import edu.ucne.regjugadores.data.jugadores.local.JugadorEntity
import edu.ucne.regjugadores.data.remote.RemoteDataSource
import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.data.remote.dto.response.jugadores.JugadorResponse
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class JugadorRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: JugadorRepositoryImpl
    private lateinit var localDataSource: JugadorDao
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = JugadorRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `createJugadorLocal guarda jugador con isPendingCreate true`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            nombres = "Juan Pérez",
            email = "juan@example.com"
        )
        val jugadorSlot = slot<JugadorEntity>()
        coEvery { localDataSource.upsert(capture(jugadorSlot)) } just Runs

        // When
        val result = repository.createJugadorLocal(jugador)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.upsert(any()) }
        assertTrue(jugadorSlot.captured.isPendingCreate)
    }

    @Test
    fun `postPendingJugadores sincroniza jugadores pendientes correctamente`() = runTest {
        // Given
        val pendingJugador = JugadorEntity(
            id = "1",
            nombres = "María García",
            email = "maria@example.com",
            isPendingCreate = true
        )
        val response = JugadorResponse(
            jugadorId = 100,
            nombres = "María García",
            email = "maria@example.com"
        )

        coEvery { localDataSource.getPendingCreateJugadores() } returns listOf(pendingJugador)
        coEvery { remoteDataSource.createJugador(any()) } returns Resource.Success(response)
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.postPendingJugadores()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.createJugador(any()) }
        coVerify {
            localDataSource.upsert(
                match { it.remoteId == 100 && !it.isPendingCreate }
            )
        }
    }

    @Test
    fun `deleteJugadorById retorna error cuando no existe remoteId`() = runTest {
        // Given
        val jugador = JugadorEntity(
            id = "1",
            remoteId = null,
            nombres = "Pedro López",
            email = "pedro@example.com"
        )
        coEvery { localDataSource.getById("1") } returns jugador

        // When
        val result = repository.deleteJugadorById("1")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Jugador no tiene remoteId", (result as Resource.Error).message)
        coVerify(exactly = 0) { remoteDataSource.deleteJugador(any()) }
    }

    @Test
    fun `deleteJugadorById elimina correctamente cuando existe remoteId`() = runTest {
        // Given
        val jugador = JugadorEntity(
            id = "1",
            remoteId = 50,
            nombres = "Ana Martínez",
            email = "ana@example.com"
        )
        coEvery { localDataSource.getById("1") } returns jugador
        coEvery { remoteDataSource.deleteJugador(50) } returns Resource.Success(Unit)
        coEvery { localDataSource.deleteById("1") } just Runs

        // When
        val result = repository.deleteJugadorById("1")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deleteJugador(50) }
        coVerify { localDataSource.deleteById("1") }
    }

    @Test
    fun `postPendingJugadores retorna error cuando falla la sincronización remota`() = runTest {
        // Given
        val pendingJugador = JugadorEntity(
            id = "1",
            nombres = "Carlos Ruiz",
            email = "carlos@example.com",
            isPendingCreate = true
        )
        coEvery { localDataSource.getPendingCreateJugadores() } returns listOf(pendingJugador)
        coEvery { remoteDataSource.createJugador(any()) } returns Resource.Error("Error de red")

        // When
        val result = repository.postPendingJugadores()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Fallo la sincronizacion", (result as Resource.Error).message)
    }

    @Test
    fun `postPendingJugadores retorna error cuando API no devuelve jugadorId`() = runTest {
        // Given
        val pendingJugador = JugadorEntity(
            id = "1",
            nombres = "Laura Sánchez",
            email = "laura@example.com",
            isPendingCreate = true
        )
        val response = JugadorResponse(
            jugadorId = null,
            nombres = "Laura Sánchez",
            email = "laura@example.com"
        )
        coEvery { localDataSource.getPendingCreateJugadores() } returns listOf(pendingJugador)
        coEvery { remoteDataSource.createJugador(any()) } returns Resource.Success(response)

        // When
        val result = repository.postPendingJugadores()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("API no devolvio el ID del jugador creado", (result as Resource.Error).message)
    }

    @Test
    fun `upsert actualiza jugador correctamente cuando tiene remoteId`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = 25,
            nombres = "Roberto Díaz",
            email = "roberto@example.com"
        )
        coEvery { remoteDataSource.updateJugador(25, any()) } returns Resource.Success(Unit)
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.upsert(jugador)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.updateJugador(25, any()) }
        coVerify { localDataSource.upsert(any()) }
    }

    @Test
    fun `upsert retorna error cuando jugador no tiene remoteId`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = null,
            nombres = "Sofía Torres",
            email = "sofia@example.com"
        )

        // When
        val result = repository.upsert(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No remoteId", (result as Resource.Error).message)
        coVerify(exactly = 0) { remoteDataSource.updateJugador(any(), any()) }
    }

    @Test
    fun `deleteJugador retorna error cuando jugador no se encuentra en BD local`() = runTest {
        // Given
        val jugador = Jugador(
            id = "999",
            nombres = "Test Usuario",
            email = "test@example.com"
        )
        coEvery { localDataSource.getById("999") } returns null

        // When
        val result = repository.deleteJugador(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message?.contains("No se encontro jugador") == true)
    }
}

