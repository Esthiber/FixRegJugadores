package edu.ucne.regjugadores.presentation.jugador

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import edu.ucne.regjugadores.domain.jugador.model.Jugador
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
class JugadoresScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun jugadoresScreenBody_muestra_loading_cuando_isLoading_es_true() {
        // Given
        val state = JugadorUiState(isLoading = true)

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun jugadoresScreenBody_muestra_mensaje_vacio_cuando_no_hay_jugadores() {
        // Given
        val state = JugadorUiState(isLoading = false, jugadores = emptyList())

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithTag("empty_message").assertIsDisplayed()
        composeTestRule.onNodeWithText("No hay jugadores").assertIsDisplayed()
    }

    @Test
    fun jugadoresScreenBody_muestra_lista_de_jugadores() {
        // Given
        val jugadores = listOf(
            Jugador(id = "1", nombres = "Juan Pérez", email = "juan@example.com"),
            Jugador(id = "2", nombres = "María García", email = "maria@example.com")
        )
        val state = JugadorUiState(isLoading = false, jugadores = jugadores)

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Juan Pérez").assertIsDisplayed()
        composeTestRule.onNodeWithText("juan@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("María García").assertIsDisplayed()
        composeTestRule.onNodeWithText("maria@example.com").assertIsDisplayed()
    }

    @Test
    fun fab_dispara_evento_ShowCreateSheet() {
        // Given
        val state = JugadorUiState(isLoading = false)
        var eventReceived: JugadorEvent? = null

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(
                    state = state,
                    onEvent = { eventReceived = it }
                )
            }
        }
        composeTestRule.onNodeWithTag("fab_add").performClick()

        // Then
        assertTrue(eventReceived is JugadorEvent.ShowCreateSheet)
    }

    @Test
    fun boton_eliminar_dispara_evento_DeleteJugador() {
        // Given
        val jugador = Jugador(id = "1", nombres = "Test", email = "test@example.com")
        val state = JugadorUiState(isLoading = false, jugadores = listOf(jugador))
        var eventReceived: JugadorEvent? = null

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(
                    state = state,
                    onEvent = { eventReceived = it }
                )
            }
        }
        composeTestRule.onNodeWithTag("btn_delete_1").performClick()

        // Then
        assertTrue(eventReceived is JugadorEvent.DeleteJugador)
        assertEquals("1", (eventReceived as JugadorEvent.DeleteJugador).id)
    }

    @Test
    fun bottomSheet_muestra_y_permite_escribir_nombre() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true,
            jugadorNombre = ""
        )
        var lastNombre = ""

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(
                    state = state,
                    onEvent = {
                        if (it is JugadorEvent.OnNombreChange) {
                            lastNombre = it.nombre
                        }
                    }
                )
            }
        }

        composeTestRule.onNodeWithTag("input_nombre")
            .performTextInput("Juan Pérez")

        // Then
        assertEquals("Juan Pérez", lastNombre)
    }

    @Test
    fun bottomSheet_muestra_y_permite_escribir_email() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true,
            jugadorEmail = ""
        )
        var lastEmail = ""

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(
                    state = state,
                    onEvent = {
                        if (it is JugadorEvent.OnEmailChange) {
                            lastEmail = it.email
                        }
                    }
                )
            }
        }

        composeTestRule.onNodeWithTag("input_email")
            .performTextInput("juan@example.com")

        // Then
        assertEquals("juan@example.com", lastEmail)
    }

    @Test
    fun boton_guardar_esta_deshabilitado_cuando_nombre_esta_vacio() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true,
            jugadorNombre = "",
            jugadorEmail = "test@example.com"
        )

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithTag("btn_save").assertIsNotEnabled()
    }

    @Test
    fun boton_guardar_esta_deshabilitado_cuando_email_esta_vacio() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true,
            jugadorNombre = "Juan Pérez",
            jugadorEmail = ""
        )

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithTag("btn_save").assertIsNotEnabled()
    }

    @Test
    fun boton_guardar_esta_habilitado_cuando_ambos_campos_tienen_valor() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true,
            jugadorNombre = "Juan Pérez",
            jugadorEmail = "juan@example.com"
        )

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithTag("btn_save").assertIsEnabled()
    }

    @Test
    fun jugador_pendiente_muestra_indicador_de_sincronizacion() {
        // Given
        val jugador = Jugador(
            id = "1",
            nombres = "Jugador Pendiente",
            email = "pendiente@example.com",
            isPendingCreate = true
        )
        val state = JugadorUiState(isLoading = false, jugadores = listOf(jugador))

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Pendiente de sincronizar").assertIsDisplayed()
    }

    @Test
    fun jugador_sincronizado_no_muestra_indicador() {
        // Given
        val jugador = Jugador(
            id = "1",
            nombres = "Jugador Sincronizado",
            email = "sincronizado@example.com",
            isPendingCreate = false
        )
        val state = JugadorUiState(isLoading = false, jugadores = listOf(jugador))

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Pendiente de sincronizar").assertDoesNotExist()
    }

    @Test
    fun bottomSheet_muestra_titulo_nuevo_jugador() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true
        )

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Nuevo Jugador").assertIsDisplayed()
    }

    @Test
    fun boton_cancelar_dispara_evento_HideCreateSheet() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true
        )
        var eventReceived: JugadorEvent? = null

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(
                    state = state,
                    onEvent = { eventReceived = it }
                )
            }
        }
        composeTestRule.onNodeWithText("Cancelar").performClick()

        // Then
        assertTrue(eventReceived is JugadorEvent.HideCreateSheet)
    }

    @Test
    fun boton_guardar_dispara_evento_CrearJugador_con_datos_correctos() {
        // Given
        val state = JugadorUiState(
            isLoading = false,
            showCreateSheet = true,
            jugadorNombre = "Carlos López",
            jugadorEmail = "carlos@example.com"
        )
        var eventReceived: JugadorEvent? = null

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(
                    state = state,
                    onEvent = { eventReceived = it }
                )
            }
        }
        composeTestRule.onNodeWithTag("btn_save").performClick()

        // Then
        assertTrue(eventReceived is JugadorEvent.CrearJugador)
        val crearEvent = eventReceived as JugadorEvent.CrearJugador
        assertEquals("Carlos López", crearEvent.nombre)
        assertEquals("carlos@example.com", crearEvent.email)
    }

    @Test
    fun jugadorItem_muestra_todos_los_datos_del_jugador() {
        // Given
        val jugador = Jugador(
            id = "1",
            remoteId = 100,
            nombres = "Ana Martínez",
            email = "ana@example.com",
            isPendingCreate = false
        )
        val state = JugadorUiState(isLoading = false, jugadores = listOf(jugador))

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Ana Martínez").assertIsDisplayed()
        composeTestRule.onNodeWithText("ana@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithTag("jugador_item_1").assertIsDisplayed()
    }

    @Test
    fun multiples_jugadores_se_muestran_en_lista() {
        // Given
        val jugadores = listOf(
            Jugador(id = "1", nombres = "Jugador 1", email = "jugador1@example.com"),
            Jugador(id = "2", nombres = "Jugador 2", email = "jugador2@example.com"),
            Jugador(id = "3", nombres = "Jugador 3", email = "jugador3@example.com")
        )
        val state = JugadorUiState(isLoading = false, jugadores = jugadores)

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithTag("jugador_item_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("jugador_item_2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("jugador_item_3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jugador 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jugador 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jugador 3").assertIsDisplayed()
    }

    @Test
    fun jugadores_pendientes_y_sincronizados_se_muestran_correctamente() {
        // Given
        val jugadores = listOf(
            Jugador(
                id = "1",
                nombres = "Jugador Sincronizado",
                email = "sync@example.com",
                isPendingCreate = false
            ),
            Jugador(
                id = "2",
                nombres = "Jugador Pendiente",
                email = "pending@example.com",
                isPendingCreate = true
            )
        )
        val state = JugadorUiState(isLoading = false, jugadores = jugadores)

        // When
        composeTestRule.setContent {
            MaterialTheme {
                JugadoresScreenBody(state = state, onEvent = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Jugador Sincronizado").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jugador Pendiente").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pendiente de sincronizar").assertIsDisplayed()
    }
}

