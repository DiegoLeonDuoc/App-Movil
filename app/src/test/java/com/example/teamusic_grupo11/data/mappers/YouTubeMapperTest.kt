package com.example.teamusic_grupo11.data.mappers

import com.example.teamusic_grupo11.data.models.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Suite de tests para YouTubeMapper.
 * Verifica que la conversión de modelos de YouTube a modelos de la aplicación funcione correctamente.
 * 
 * El mapper convierte:
 * - YouTubeVideoItem -> Song (para videos de tendencias)
 * - YouTubeSearchItem -> Song (para resultados de búsqueda)
 */
class YouTubeMapperTest : DescribeSpec({
    
    describe("YouTubeMapper") {
        
        describe("fromVideoItem") {
            it("debería mapear YouTubeVideoItem a Song correctamente") {
                // ===== GIVEN =====
                // Paso 1: Crear un YouTubeVideoItem con todos los datos necesarios
                val videoItem = YouTubeVideoItem(
                    id = "123",
                    snippet = Snippet(
                        publishedAt = "2023-01-01",
                        channelId = "channel123",
                        title = "Test Song",
                        description = "Test Description",
                        thumbnails = Thumbnails(
                            high = Thumbnail("https://example.com/thumb_high.jpg"),
                            medium = Thumbnail("https://example.com/thumb_med.jpg"),
                            default = Thumbnail("https://example.com/thumb_def.jpg")
                        ),
                        channelTitle = "Test Artist"
                    )
                )
                
                // ===== WHEN =====
                // Paso 2: Convertir el YouTubeVideoItem a Song usando el mapper
                val song = YouTubeMapper.fromVideoItem(videoItem)
                
                // ===== THEN =====
                // Paso 3: Verificar que el título se mapeó correctamente
                song.title shouldBe "Test Song"
                // Paso 4: Verificar que el artista (channelTitle) se mapeó correctamente
                song.artist shouldBe "Test Artist"
                // Paso 5: Verificar que se usó la miniatura de alta calidad
                song.imageRes shouldBe "https://example.com/thumb_high.jpg"
            }
            
            it("debería manejar miniatura de alta calidad faltante") {
                // ===== GIVEN =====
                // Paso 1: Crear un YouTubeVideoItem SIN miniatura de alta calidad
                val videoItem = YouTubeVideoItem(
                    id = "123",
                    snippet = Snippet(
                        publishedAt = "2023-01-01",
                        channelId = "channel123",
                        title = "Test Song",
                        description = "Test Description",
                        thumbnails = Thumbnails(
                            high = null,  // No hay miniatura de alta calidad
                            medium = Thumbnail("https://example.com/thumb_med.jpg"),
                            default = Thumbnail("https://example.com/thumb_def.jpg")
                        ),
                        channelTitle = "Test Artist"
                    )
                )
                
                // ===== WHEN =====
                // Paso 2: Convertir a Song
                val song = YouTubeMapper.fromVideoItem(videoItem)
                
                // ===== THEN =====
                // Paso 3: Verificar que se usó la miniatura de calidad media como fallback
                song.imageRes shouldBe "https://example.com/thumb_med.jpg"
            }
        }
        
        describe("fromSearchItem") {
            it("debería mapear YouTubeSearchItem a Song correctamente") {
                // ===== GIVEN =====
                // Paso 1: Crear un YouTubeSearchItem de tipo video
                val searchItem = YouTubeSearchItem(
                    id = YouTubeId(kind = "youtube#video", videoId = "123"),
                    snippet = Snippet(
                        publishedAt = "2023-01-01",
                        channelId = "channel123",
                        title = "Search Song",
                        description = "Search Description",
                        thumbnails = Thumbnails(
                            high = Thumbnail("https://example.com/thumb_high.jpg")
                        ),
                        channelTitle = "Search Artist"
                    )
                )
                
                // ===== WHEN =====
                // Paso 2: Convertir el SearchItem a Song
                val song = YouTubeMapper.fromSearchItem(searchItem)
                
                // ===== THEN =====
                // Paso 3: Verificar que el título se mapeó correctamente
                song?.title shouldBe "Search Song"
                // Paso 4: Verificar que el artista se mapeó correctamente
                song?.artist shouldBe "Search Artist"
                // Paso 5: Verificar que la imagen se mapeó correctamente
                song?.imageRes shouldBe "https://example.com/thumb_high.jpg"
            }
            
            it("debería retornar null si falta el videoId") {
                // ===== GIVEN =====
                // Paso 1: Crear un SearchItem de tipo CHANNEL (no video)
                // Los canales no tienen videoId, solo channelId
                val searchItem = YouTubeSearchItem(
                    id = YouTubeId(kind = "youtube#channel", channelId = "channel123"), // No videoId
                    snippet = Snippet(
                        publishedAt = "2023-01-01",
                        channelId = "channel123",
                        title = "Channel Result",
                        description = "Channel Description",
                        thumbnails = Thumbnails(default = Thumbnail("url")),
                        channelTitle = "Channel Name"
                    )
                )
                
                // ===== WHEN =====
                // Paso 2: Intentar convertir a Song
                val song = YouTubeMapper.fromSearchItem(searchItem)
                
                // ===== THEN =====
                // Paso 3: Verificar que retorna null porque no es un video
                // (solo los videos pueden convertirse a canciones)
                song shouldBe null
            }
        }
    }
})
