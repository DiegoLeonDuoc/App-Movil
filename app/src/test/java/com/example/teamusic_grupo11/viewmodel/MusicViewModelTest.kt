package com.example.teamusic_grupo11.viewmodel

import app.cash.turbine.test
import com.example.teamusic_grupo11.data.models.*
import com.example.teamusic_grupo11.data.repository.YouTubeRepository
import com.example.teamusic_grupo11.network.NetworkResult
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest : DescribeSpec({
    val testDispatcher = StandardTestDispatcher()
    
    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }
    
    afterSpec {
        Dispatchers.resetMain()
    }
    
    describe("MusicViewModel") {
        val mockRepository = mockk<YouTubeRepository>()
        
        describe("loadTrending") {
            it("should load trending songs successfully") {
                // Given
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
                coEvery { mockRepository.getTrending() } returns NetworkResult.Success(mockResponse)
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading // Mock recommended too as init calls it
                
                // When
                val viewModel = MusicViewModel(mockRepository)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // Then
                coVerify { mockRepository.getTrending() }
                viewModel.uiState.value.trendingSongs shouldHaveSize 2
            }
            
            it("should handle error when loading trending") {
                // Given
                coEvery { mockRepository.getTrending() } returns NetworkResult.Error("Network error")
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                
                // When
                val viewModel = MusicViewModel(mockRepository)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // Then
                viewModel.uiState.value.errorMessage shouldBe "Network error"
            }
        }
        
        describe("searchMusic") {
            it("should search for music successfully") {
                // Given
                val query = "test query"
                val mockItems = listOf(
                    YouTubeSearchItem(
                        YouTubeId("video", "3"),
                        Snippet("2023", "channel", "Search Result", "Desc", Thumbnails(), "Artist 3")
                    )
                )
                val mockResponse = SearchResponse("kind", "etag", null, "US", PageInfo(1, 1), mockItems)
                coEvery { mockRepository.searchMusic(query) } returns NetworkResult.Success(mockResponse)
                coEvery { mockRepository.getTrending() } returns NetworkResult.Loading // Mock init calls
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                
                // When
                val viewModel = MusicViewModel(mockRepository)
                viewModel.searchMusic(query)
                testDispatcher.scheduler.advanceUntilIdle()
                
                // Then
                coVerify { mockRepository.searchMusic(query) }
                viewModel.uiState.value.searchResults shouldHaveSize 1
            }
            
            it("should clear search results when query is blank") {
                // Given
                coEvery { mockRepository.getTrending() } returns NetworkResult.Loading
                coEvery { mockRepository.getRecommended() } returns NetworkResult.Loading
                val viewModel = MusicViewModel(mockRepository)
                
                // When
                viewModel.searchMusic("")
                testDispatcher.scheduler.advanceUntilIdle()
                
                // Then
                viewModel.uiState.value.searchResults shouldHaveSize 0
            }
        }
    }
})
