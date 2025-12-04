package com.example.teamusic_grupo11.viewmodel

import app.cash.turbine.test
import com.example.teamusic_grupo11.dataDAO.Song
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Suite de tests para PlayerViewModel.
 * Prueba la funcionalidad del reproductor de música incluyendo:
 * - Reproducción de canciones
 * - Control de play/pause
 * - Navegación (siguiente/anterior)
 * - Búsqueda en la línea de tiempo (seekTo)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest : DescribeSpec({
    // Dispatcher de prueba para controlar coroutines
    val testDispatcher = StandardTestDispatcher()
    
    // Configuración antes de todos los tests
    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }
    
    // Limpieza después de todos los tests
    afterSpec {
        Dispatchers.resetMain()
    }
    
    describe("PlayerViewModel") {
        // Crear una instancia del ViewModel para las pruebas
        val viewModel = PlayerViewModel()
        
        describe("playSong") {
            it("debería actualizar la canción actual y establecer reproducción en verdadero") {
                // ===== GIVEN =====
                // Paso 1: Crear una canción de prueba
                val song = Song("Test Song", "Test Artist", "test.jpg")
                
                // ===== WHEN =====
                // Paso 2: Reproducir la canción
                viewModel.playSong(song)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 3: Verificar que la canción actual es la que se pasó
                viewModel.playerState.value.currentSong shouldBe song
                // Paso 4: Verificar que el estado de reproducción es verdadero
                viewModel.playerState.value.isPlaying shouldBe true
                // Paso 5: Verificar que la posición se reinició a 0
                viewModel.playerState.value.currentPosition shouldBe 0f
            }
        }
        
        describe("togglePlayPause") {
            it("debería alternar el estado de reproducción") {
                // ===== GIVEN =====
                // Paso 1: Obtener el estado inicial de reproducción
                val initialState = viewModel.playerState.value.isPlaying
                
                // ===== WHEN =====
                // Paso 2: Alternar el estado de reproducción
                viewModel.togglePlayPause()
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 3: Verificar que el estado cambió al opuesto
                viewModel.playerState.value.isPlaying shouldBe !initialState
            }
            
            it("debería alternar múltiples veces correctamente") {
                // ===== GIVEN/WHEN =====
                // Paso 1: Primera alternancia
                viewModel.togglePlayPause()
                testDispatcher.scheduler.advanceUntilIdle()
                val firstToggle = viewModel.playerState.value.isPlaying
                
                // Paso 2: Segunda alternancia
                viewModel.togglePlayPause()
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 3: Verificar que volvió al estado opuesto
                viewModel.playerState.value.isPlaying shouldBe !firstToggle
            }
        }
        
        describe("seekTo") {
            it("debería actualizar la posición y el tiempo") {
                // ===== GIVEN =====
                // Paso 1: Definir una posición objetivo (50% de la canción)
                val position = 0.5f // 50%
                
                // ===== WHEN =====
                // Paso 2: Buscar a esa posición
                viewModel.seekTo(position)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 3: Verificar que la posición se actualizó
                viewModel.playerState.value.currentPosition shouldBe position
                // Paso 4: Verificar que el tiempo en milisegundos corresponde a la posición
                viewModel.playerState.value.currentTimeMs shouldBe (position * viewModel.playerState.value.durationMs).toLong()
            }
            
            it("debería manejar casos extremos") {
                // ===== GIVEN/WHEN/THEN =====
                // Caso 1: Buscar al inicio (0%)
                viewModel.seekTo(0f)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.playerState.value.currentPosition shouldBe 0f
                
                // Caso 2: Buscar al final (100%)
                viewModel.seekTo(1f)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.playerState.value.currentPosition shouldBe 1f
            }
        }
        
        describe("playNext") {
            it("debería reiniciar la posición") {
                // ===== GIVEN =====
                // Paso 1: Mover la posición a la mitad de la canción
                viewModel.seekTo(0.5f)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== WHEN =====
                // Paso 2: Reproducir la siguiente canción
                viewModel.playNext()
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 3: Verificar que la posición se reinició a 0
                viewModel.playerState.value.currentPosition shouldBe 0f
                // Paso 4: Verificar que el tiempo también se reinició
                viewModel.playerState.value.currentTimeMs shouldBe 0L
            }
        }
        
        describe("playPrevious") {
            it("debería reiniciar la posición") {
                // ===== GIVEN =====
                // Paso 1: Mover la posición al 70% de la canción
                viewModel.seekTo(0.7f)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== WHEN =====
                // Paso 2: Reproducir la canción anterior
                viewModel.playPrevious()
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 3: Verificar que la posición se reinició a 0
                viewModel.playerState.value.currentPosition shouldBe 0f
                // Paso 4: Verificar que el tiempo también se reinició
                viewModel.playerState.value.currentTimeMs shouldBe 0L
            }
        }
        
        describe("state flow") {
            it("debería emitir cambios de estado") {
                // Este test verifica que el StateFlow emite valores cuando cambia el estado
                viewModel.playerState.test {
                    // Paso 1: Obtener el estado inicial
                    val initialState = awaitItem()
                    initialState.currentSong shouldNotBe null
                    
                    // Paso 2: Reproducir una nueva canción
                    val song = Song("Flow Test", "Artist", "image.jpg")
                    viewModel.playSong(song)
                    testDispatcher.scheduler.advanceUntilIdle()
                    
                    // Paso 3: Esperar y verificar el nuevo estado emitido
                    val newState = awaitItem()
                    newState.currentSong shouldNotBe null
                    newState.isPlaying shouldBe true
                    
                    // Paso 4: Cancelar y ignorar eventos restantes
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }
})
