package com.example.teamusic_grupo11.data.repository

import com.example.teamusic_grupo11.data.models.*
import com.example.teamusic_grupo11.network.NetworkResult
import com.example.teamusic_grupo11.network.YouTubeApiService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

/**
 * Suite de tests para YouTubeRepository.
 * Verifica que el repositorio maneje correctamente:
 * - Llamadas exitosas a la API
 * - Errores de red
 * - Respuestas nulas
 * 
 * Utiliza MockK para simular el comportamiento del servicio de API.
 */
class YouTubeRepositoryTest : DescribeSpec({
    
    describe("YouTubeRepository") {
        // Paso 1: Crear un mock del servicio de API de YouTube
        val mockApiService = mockk<YouTubeApiService>()
        // Paso 2: Crear el repositorio con el servicio mockeado
        val repository = YouTubeRepository(mockApiService)
        
        describe("searchMusic") {
            it("debería retornar éxito cuando la llamada a la API tiene éxito") {
                // ===== GIVEN =====
                val query = "test"
                
                // Paso 1: Crear datos de respuesta simulados
                val mockItems = listOf(
                    YouTubeSearchItem(
                        YouTubeId("youtube#video", "123"),
                        Snippet("2023", "channel", "Title", "Desc", Thumbnails(), "Channel")
                    )
                )
                val mockResponse = SearchResponse("youtube#searchListResponse", "etag", null, "US", PageInfo(1, 1), mockItems)
                
                // Paso 2: Configurar el mock para retornar una respuesta exitosa
                coEvery { mockApiService.searchMusic(query, any(), any(), any(), any()) } returns Response.success(mockResponse)
                
                // ===== WHEN =====
                // Paso 3: Ejecutar la búsqueda
                val result = repository.searchMusic(query)
                
                // ===== THEN =====
                // Paso 4: Verificar que el resultado es de tipo Success
                result.shouldBeInstanceOf<NetworkResult.Success<SearchResponse>>()
                // Paso 5: Verificar que contiene 1 item
                (result as NetworkResult.Success).data.items.size shouldBe 1
            }
            
            it("debería retornar error cuando la llamada a la API falla") {
                // ===== GIVEN =====
                val query = "test"
                
                // Paso 1: Configurar el mock para retornar un error HTTP 404
                coEvery { mockApiService.searchMusic(query, any(), any(), any(), any()) } returns Response.error(404, mockk(relaxed = true))
                
                // ===== WHEN =====
                // Paso 2: Ejecutar la búsqueda
                val result = repository.searchMusic(query)
                
                // ===== THEN =====
                // Paso 3: Verificar que el resultado es de tipo Error
                result.shouldBeInstanceOf<NetworkResult.Error>()
            }
        }
        
        describe("getTrending") {
            it("debería retornar videos en tendencia en caso de éxito") {
                // ===== GIVEN =====
                // Paso 1: Crear datos de videos en tendencia simulados
                val mockItems = listOf(
                    YouTubeVideoItem(
                        "123",
                        Snippet("2023", "channel", "Title", "Desc", Thumbnails(), "Channel")
                    )
                )
                val mockResponse = VideoListResponse("youtube#videoListResponse", "etag", mockItems, PageInfo(1, 1))
                
                // Paso 2: Configurar el mock para retornar videos en tendencia
                coEvery { mockApiService.getTrending(any(), any(), any(), any(), any()) } returns Response.success(mockResponse)
                
                // ===== WHEN =====
                // Paso 3: Obtener videos en tendencia
                val result = repository.getTrending()
                
                // ===== THEN =====
                // Paso 4: Verificar que el resultado es exitoso
                result.shouldBeInstanceOf<NetworkResult.Success<VideoListResponse>>()
                // Paso 5: Verificar que contiene 1 video
                (result as NetworkResult.Success).data.items.size shouldBe 1
            }
        }
        
        describe("error handling") {
            it("debería manejar cuerpo de respuesta nulo") {
                // ===== GIVEN =====
                // Paso 1: Configurar el mock para retornar una respuesta exitosa pero con cuerpo nulo
                // Esto puede ocurrir en casos raros de problemas de red o servidor
                coEvery { mockApiService.getTrending(any(), any(), any(), any(), any()) } returns Response.success(null)
                
                // ===== WHEN =====
                // Paso 2: Intentar obtener videos en tendencia
                val result = repository.getTrending()
                
                // ===== THEN =====
                // Paso 3: Verificar que el resultado es un error
                result.shouldBeInstanceOf<NetworkResult.Error>()
                // Paso 4: Verificar que el mensaje de error es el esperado
                (result as NetworkResult.Error).message shouldBe "Cuerpo de respuesta vacío"
            }
        }
    }
})
