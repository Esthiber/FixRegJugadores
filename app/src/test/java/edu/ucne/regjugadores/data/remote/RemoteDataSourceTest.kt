package edu.ucne.regjugadores.data.remote

import edu.ucne.regjugadores.data.remote.dto.request.jugadores.JugadorRequest
import edu.ucne.regjugadores.data.remote.dto.response.jugadores.JugadorResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    private lateinit var dataSource: RemoteDataSource
    private lateinit var api: TicTacToeApi

    @Before
    fun setup() {
        api = mockk()
        dataSource = RemoteDataSource(api)
    }

    @Test
    fun `createJugador retorna Success cuando API responde 200`() = runTest {
        // Given
        val request = JugadorRequest("Juan Pérez", "juan@example.com")
        val response = JugadorResponse(1, "Juan Pérez", "juan@example.com")
        coEvery { api.createJugador(request) } returns Response.success(response)

        // When
        val result = dataSource.createJugador(request)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(response, (result as Resource.Success).data)
    }

    @Test
    fun `createJugador retorna Error cuando API falla`() = runTest {
        // Given
        val request = JugadorRequest("María García", "maria@example.com")
        coEvery { api.createJugador(request) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = dataSource.createJugador(request)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("HTTP 500"))
    }

    @Test
    fun `createJugador retorna Error cuando hay excepción de red`() = runTest {
        // Given
        val request = JugadorRequest("Pedro López", "pedro@example.com")
        coEvery { api.createJugador(request) } throws IOException("Network error")

        // When
        val result = dataSource.createJugador(request)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Network error"))
    }

    @Test
    fun `createJugador retorna Error cuando body es null`() = runTest {
        // Given
        val request = JugadorRequest("Ana Martínez", "ana@example.com")
        coEvery { api.createJugador(request) } returns Response.success(null)

        // When
        val result = dataSource.createJugador(request)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Respuesta vacia del servidor", (result as Resource.Error).message)
    }

    @Test
    fun `createJugador extrae jugadorId del header Location cuando es null en body`() = runTest {
        // Given
        val request = JugadorRequest("Carlos Ruiz", "carlos@example.com")
        val responseWithoutId = JugadorResponse(null, "Carlos Ruiz", "carlos@example.com")
        val headers = okhttp3.Headers.Builder()
            .add("Location", "https://api.example.com/jugadores?id=42")
            .build()

        coEvery { api.createJugador(request) } returns Response.success(
            responseWithoutId,
            headers
        )

        // When
        val result = dataSource.createJugador(request)

        // Then
        assertTrue(result is Resource.Success)
        val successResult = result as Resource.Success
        assertEquals(42, successResult.data?.jugadorId)
        assertEquals("Carlos Ruiz", successResult.data?.nombres)
    }

    @Test
    fun `updateJugador retorna Success cuando API responde 200`() = runTest {
        // Given
        val jugadorId = 1
        val request = JugadorRequest("Juan Pérez Actualizado", "juan.new@example.com")
        coEvery { api.updateJugador(jugadorId, request) } returns Response.success(Unit)

        // When
        val result = dataSource.updateJugador(jugadorId, request)

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `updateJugador retorna Error cuando API falla`() = runTest {
        // Given
        val jugadorId = 1
        val request = JugadorRequest("Juan Pérez", "juan@example.com")
        coEvery { api.updateJugador(jugadorId, request) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = dataSource.updateJugador(jugadorId, request)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("HTTP 404"))
    }

    @Test
    fun `updateJugador retorna Error cuando hay excepción de red`() = runTest {
        // Given
        val jugadorId = 1
        val request = JugadorRequest("Laura Sánchez", "laura@example.com")
        coEvery { api.updateJugador(jugadorId, request) } throws IOException("Connection timeout")

        // When
        val result = dataSource.updateJugador(jugadorId, request)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Connection timeout"))
    }

    @Test
    fun `deleteJugador retorna Success cuando API responde 200`() = runTest {
        // Given
        val jugadorId = 1
        coEvery { api.deleteJugador(jugadorId) } returns Response.success(Unit)

        // When
        val result = dataSource.deleteJugador(jugadorId)

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `deleteJugador retorna Error cuando API falla`() = runTest {
        // Given
        val jugadorId = 999
        coEvery { api.deleteJugador(jugadorId) } returns Response.error(
            404,
            "Jugador no encontrado".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = dataSource.deleteJugador(jugadorId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("HTTP 404"))
    }

    @Test
    fun `deleteJugador retorna Error cuando hay excepción de red`() = runTest {
        // Given
        val jugadorId = 1
        coEvery { api.deleteJugador(jugadorId) } throws IOException("Network unavailable")

        // When
        val result = dataSource.deleteJugador(jugadorId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Network unavailable"))
    }

    @Test
    fun `createJugador maneja error genérico cuando excepción no tiene mensaje`() = runTest {
        // Given
        val request = JugadorRequest("Test User", "test@example.com")
        coEvery { api.createJugador(request) } throws RuntimeException()

        // When
        val result = dataSource.createJugador(request)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Ocurrio un error desconocido"))
    }
}

