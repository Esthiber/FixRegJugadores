package edu.ucne.regjugadores.domain.jugador.usecase

import edu.ucne.regjugadores.data.remote.Resource
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import edu.ucne.regjugadores.domain.jugador.repository.JugadorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpsertJugadorUseCaseTest {

    private lateinit var useCase: UpsertJugadorUseCase
    private lateinit var repository: JugadorRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertJugadorUseCase(repository)
    }

    @Test
    fun `invoke actualiza jugador con datos validos`() = runTest {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = 100,
            nombres = "Juan Pérez",
            email = "juan@example.com"
        )
        coEvery { repository.upsert(jugador) } returns Resource.Success(Unit)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { repository.upsert(jugador) }
    }

    @Test
    fun `invoke retorna error cuando nombre esta vacio`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "",
            email = "test@example.com"
        )

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("nombre no puede estar vacio"))
    }

    @Test
    fun `invoke retorna error cuando nombre tiene menos de 3 caracteres`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "AB",
            email = "test@example.com"
        )

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("al menos 3 caracteres"))
    }

    @Test
    fun `invoke retorna error cuando nombre tiene mas de 50 caracteres`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "A".repeat(51),
            email = "test@example.com"
        )

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("mas de 50 caracteres"))
    }

    @Test
    fun `invoke retorna error cuando email esta vacio`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "Juan Pérez",
            email = ""
        )

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("email no puede estar vacio"))
    }

    @Test
    fun `invoke retorna error cuando email no es valido`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "Juan Pérez",
            email = "correo-invalido"
        )

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("email no es valido"))
    }

    @Test
    fun `invoke acepta email valido con formato correcto`() = runTest {
        // Given
        val jugador = Jugador(
            id = "2",
            remoteId = 200,
            nombres = "María García",
            email = "maria.garcia@example.com"
        )
        coEvery { repository.upsert(jugador) } returns Resource.Success(Unit)

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { repository.upsert(jugador) }
    }

    @Test
    fun `invoke propaga errores del repositorio cuando las validaciones pasan`() = runTest {
        // Given
        val jugador = Jugador(
            nombres = "Pedro López",
            email = "pedro@example.com"
        )
        coEvery { repository.upsert(jugador) } returns Resource.Error("No remoteId")

        // When
        val result = useCase(jugador)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("No remoteId"))
    }

    @Test
    fun `invoke valida nombre antes de email`() = runTest {
        // Given - Ambos inválidos
        val jugador = Jugador(
            nombres = "",
            email = "invalido"
        )

        // When
        val result = useCase(jugador)

        // Then - Debe retornar el error del nombre primero
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("nombre"))
    }
}

