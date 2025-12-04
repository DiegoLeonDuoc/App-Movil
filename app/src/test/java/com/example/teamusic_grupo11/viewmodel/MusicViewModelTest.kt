package com.example.teamusic_grupo11.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import app.cash.turbine.test
import com.example.teamusic_grupo11.data.SELECTED_REGION
import com.example.teamusic_grupo11.data.dataStore
import com.example.teamusic_grupo11.data.models.*
import com.example.teamusic_grupo11.data.repository.YouTubeRepository
import com.example.teamusic_grupo11.network.NetworkResult
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Suite de tests para MusicViewModel.
 * Utiliza Kotest como framework de testing y MockK para crear mocks.
 * 
 * @OptIn(ExperimentalCoroutinesApi::class) - Permite usar APIs experimentales de coroutines para testing
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest : DescribeSpec({
    // Dispatcher de prueba para controlar la ejecución de coroutines en los tests
    val testDispatcher = StandardTestDispatcher()
    
    // beforeSpec se ejecuta UNA VEZ antes de todos los tests
    beforeSpec {
        // Configurar el dispatcher principal para usar nuestro testDispatcher
        // Esto permite controlar cuándo se ejecutan las coroutines en los tests
        Dispatchers.setMain(testDispatcher)
    }
    
    // afterSpec se ejecuta UNA VEZ después de todos los tests
    afterSpec {
        // Restaurar el dispatcher principal al valor por defecto
        Dispatchers.resetMain()
    }
    
    describe("MusicViewModel") {
        // Crear un mock del repositorio que será usado en todos los tests
        val mockRepository = mockk<YouTubeRepository>()
        
        // Crear mocks para Context y DataStore
        // relaxed = true permite que Context retorne valores por defecto para métodos como getApplicationContext()
        val mockContext = mockk<Context>(relaxed = true)
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        
        // Configurar el mock de DataStore
        beforeSpec {
            // Mockear la propiedad de extensión dataStore
            mockkStatic("com.example.teamusic_grupo11.data.DataStoreModuleKt")
            every { any<Context>().dataStore } returns mockDataStore
            coEvery { mockDataStore.data } returns flowOf(preferencesOf())
        }
        
        afterSpec {
            // Limpiar los mocks estáticos
            unmockkStatic("com.example.teamusic_grupo11.data.DataStoreModuleKt")
        }
        
        describe("loadTrending") {
            it("debería cargar canciones en tendencia exitosamente") {
                // ===== GIVEN (Dado) - Preparar el escenario del test =====
                
                // Paso 1: Crear datos de prueba (mock items)
                val mockItems = listOf(
                    YouTubeVideoItem(
                        "1",
                        Snippet("2023", "channel", "Song 1", "Desc", Thumbnails(), "Artist 1")
                    ),
                    YouTubeVideoItem(
                        "2",
                        Snippet("2023", "channel", "Song 2", "Desc", Thumbnails(), "Artist 2")
                    )
                )
                val mockResponse = VideoListResponse("kind", "etag", mockItems, PageInfo(2, 2))
                
                // Paso 2: Configurar el comportamiento del mock
                // Cuando se llame a getTrending() con cualquier región, retornar un resultado exitoso
                coEvery { mockRepository.getTrending(any()) } returns NetworkResult.Success(mockResponse)
                // También configurar getRecommended() porque el init del ViewModel lo llama
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                
                // ===== WHEN (Cuando) - Ejecutar la acción a probar =====
                
                // Paso 3: Crear el ViewModel (esto automáticamente llama a loadTrending en el init)
                val viewModel = MusicViewModel(mockContext, mockRepository)
                
                // Paso 4: Avanzar el dispatcher hasta que todas las coroutines terminen
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN (Entonces) - Verificar los resultados =====
                
                // Paso 5: Verificar que se llamó al método getTrending del repositorio
                // Se llama con "CL" porque es la región por defecto
                coVerify { mockRepository.getTrending("CL") }
                
                // Paso 6: Verificar que el estado del ViewModel contiene 2 canciones
                viewModel.uiState.value.trendingSongs shouldHaveSize 2
            }
            
            it("debería manejar errores al cargar tendencias") {
                // ===== GIVEN =====
                // Configurar el mock para retornar un error
                coEvery { mockRepository.getTrending(any()) } returns NetworkResult.Error("Network error")
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                
                // ===== WHEN =====
                val viewModel = MusicViewModel(mockContext, mockRepository)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Verificar que el mensaje de error se guardó en el estado
                viewModel.uiState.value.errorMessage shouldBe "Network error"
            }
        }
        
        describe("searchMusic") {
            it("debería buscar música exitosamente") {
                // ===== GIVEN =====
                val query = "test query"
                
                // Paso 1: Crear datos de búsqueda simulados
                val mockItems = listOf(
                    YouTubeSearchItem(
                        YouTubeId("video", "3"),
                        Snippet("2023", "channel", "Search Result", "Desc", Thumbnails(), "Artist 3")
                    )
                )
                val mockResponse = SearchResponse("kind", "etag", null, "US", PageInfo(1, 1), mockItems)
                
                // Paso 2: Configurar mocks
                coEvery { mockRepository.searchMusic(query) } returns NetworkResult.Success(mockResponse)
                coEvery { mockRepository.getTrending(any()) } returns NetworkResult.Loading
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                
                // ===== WHEN =====
                val viewModel = MusicViewModel(mockContext, mockRepository)
                // Paso 3: Ejecutar la búsqueda
                viewModel.searchMusic(query)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Paso 4: Verificar que se llamó al método de búsqueda
                coVerify { mockRepository.searchMusic(query) }
                // Paso 5: Verificar que hay 1 resultado de búsqueda
                viewModel.uiState.value.searchResults shouldHaveSize 1
            }
            
            it("debería limpiar resultados de búsqueda cuando la consulta está vacía") {
                // ===== GIVEN =====
                coEvery { mockRepository.getTrending(any()) } returns NetworkResult.Loading
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                val viewModel = MusicViewModel(mockContext, mockRepository)
                
                // ===== WHEN =====
                // Buscar con una cadena vacía
                viewModel.searchMusic("")
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Los resultados de búsqueda deben estar vacíos
                viewModel.uiState.value.searchResults shouldHaveSize 0
            }
        }
        
        describe("setRegion") {
            it("debería cambiar la región y recargar tendencias") {
                // ===== GIVEN =====
                // Configurar mocks para las llamadas iniciales
                val initialMockItems = listOf(
                    YouTubeVideoItem("1", Snippet("2023", "ch", "CL Song", "Desc", Thumbnails(), "Artist"))
                )
                val initialResponse = VideoListResponse("kind", "etag", initialMockItems, PageInfo(1, 1))
                
                // Configurar mocks para la nueva región
                val usMockItems = listOf(
                    YouTubeVideoItem("2", Snippet("2023", "ch", "US Song", "Desc", Thumbnails(), "Artist"))
                )
                val usResponse = VideoListResponse("kind", "etag", usMockItems, PageInfo(1, 1))
                
                // Primera llamada retorna canciones de Chile, segunda de USA
                coEvery { mockRepository.getTrending("CL") } returns NetworkResult.Success(initialResponse)
                coEvery { mockRepository.getTrending("US") } returns NetworkResult.Success(usResponse)
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                
                val viewModel = MusicViewModel(mockContext, mockRepository)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== WHEN =====
                // Cambiar la región a Estados Unidos
                viewModel.setRegion("US")
                testDispatcher.scheduler.advanceUntilIdle()
                
                // ===== THEN =====
                // Verificar que la región se actualizó en el estado
                viewModel.uiState.value.selectedRegion shouldBe "US"
                
                // Verificar que se llamó a getTrending con la nueva región
                coVerify { mockRepository.getTrending("US") }
                
                // Nota: No verificamos updateData directamente porque con relaxed mocking
                // es difícil de mockear correctamente. Lo importante es que la región
                // se actualiza en el estado y se recargan las tendencias.
            }
        }
    }
})
