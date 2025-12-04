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

class YouTubeRepositoryTest : DescribeSpec({
    
    describe("YouTubeRepository") {
        val mockApiService = mockk<YouTubeApiService>()
        val repository = YouTubeRepository(mockApiService)
        
        describe("searchMusic") {
            it("should return success when API call succeeds") {
                // Given
                val query = "test"
                val mockItems = listOf(
                    YouTubeSearchItem(
                        YouTubeId("youtube#video", "123"),
                        Snippet("2023", "channel", "Title", "Desc", Thumbnails(), "Channel")
                    )
                )
                val mockResponse = SearchResponse("youtube#searchListResponse", "etag", null, "US", PageInfo(1, 1), mockItems)
                coEvery { mockApiService.searchMusic(query, any(), any(), any(), any()) } returns Response.success(mockResponse)
                
                // When
                val result = repository.searchMusic(query)
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Success<SearchResponse>>()
                (result as NetworkResult.Success).data.items.size shouldBe 1
            }
            
            it("should return error when API call fails") {
                // Given
                val query = "test"
                coEvery { mockApiService.searchMusic(query, any(), any(), any(), any()) } returns Response.error(404, mockk(relaxed = true))
                
                // When
                val result = repository.searchMusic(query)
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Error>()
            }
        }
        
        describe("getTrending") {
            it("should return trending videos on success") {
                // Given
                val mockItems = listOf(
                    YouTubeVideoItem(
                        "123",
                        Snippet("2023", "channel", "Title", "Desc", Thumbnails(), "Channel")
                    )
                )
                val mockResponse = VideoListResponse("youtube#videoListResponse", "etag", mockItems, PageInfo(1, 1))
                coEvery { mockApiService.getTrending(any(), any(), any(), any(), any()) } returns Response.success(mockResponse)
                
                // When
                val result = repository.getTrending()
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Success<VideoListResponse>>()
                (result as NetworkResult.Success).data.items.size shouldBe 1
            }
        }
        
        describe("error handling") {
            it("should handle null response body") {
                // Given
                coEvery { mockApiService.getTrending(any(), any(), any(), any(), any()) } returns Response.success(null)
                
                // When
                val result = repository.getTrending()
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Error>()
                (result as NetworkResult.Error).message shouldBe "Empty response body"
            }
        }
    }
})
