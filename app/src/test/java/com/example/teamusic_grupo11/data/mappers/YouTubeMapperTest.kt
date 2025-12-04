package com.example.teamusic_grupo11.data.mappers

import com.example.teamusic_grupo11.data.models.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class YouTubeMapperTest : DescribeSpec({
    
    describe("YouTubeMapper") {
        
        describe("fromVideoItem") {
            it("should map YouTubeVideoItem to Song correctly") {
                // Given
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
                
                // When
                val song = YouTubeMapper.fromVideoItem(videoItem)
                
                // Then
                song.title shouldBe "Test Song"
                song.artist shouldBe "Test Artist"
                song.imageRes shouldBe "https://example.com/thumb_high.jpg"
            }
            
            it("should handle missing high quality thumbnail") {
                // Given
                val videoItem = YouTubeVideoItem(
                    id = "123",
                    snippet = Snippet(
                        publishedAt = "2023-01-01",
                        channelId = "channel123",
                        title = "Test Song",
                        description = "Test Description",
                        thumbnails = Thumbnails(
                            high = null,
                            medium = Thumbnail("https://example.com/thumb_med.jpg"),
                            default = Thumbnail("https://example.com/thumb_def.jpg")
                        ),
                        channelTitle = "Test Artist"
                    )
                )
                
                // When
                val song = YouTubeMapper.fromVideoItem(videoItem)
                
                // Then
                song.imageRes shouldBe "https://example.com/thumb_med.jpg"
            }
        }
        
        describe("fromSearchItem") {
            it("should map YouTubeSearchItem to Song correctly") {
                // Given
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
                
                // When
                val song = YouTubeMapper.fromSearchItem(searchItem)
                
                // Then
                song?.title shouldBe "Search Song"
                song?.artist shouldBe "Search Artist"
                song?.imageRes shouldBe "https://example.com/thumb_high.jpg"
            }
            
            it("should return null if videoId is missing") {
                // Given
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
                
                // When
                val song = YouTubeMapper.fromSearchItem(searchItem)
                
                // Then
                song shouldBe null
            }
        }
    }
})
