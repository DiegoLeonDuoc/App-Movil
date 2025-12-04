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

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest : DescribeSpec({
    val testDispatcher = StandardTestDispatcher()
    
    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }
    
    afterSpec {
        Dispatchers.resetMain()
    }
    
    describe("PlayerViewModel") {
        val viewModel = PlayerViewModel()
        
        describe("playSong") {
            it("should update current song and set playing to true") {
                val song = Song("Test Song", "Test Artist", "test.jpg")
                
                viewModel.playSong(song)
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playerState.value.currentSong shouldBe song
                viewModel.playerState.value.isPlaying shouldBe true
                viewModel.playerState.value.currentPosition shouldBe 0f
            }
        }
        
        describe("togglePlayPause") {
            it("should toggle play state") {
                val initialState = viewModel.playerState.value.isPlaying
                
                viewModel.togglePlayPause()
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playerState.value.isPlaying shouldBe !initialState
            }
            
            it("should toggle multiple times correctly") {
                viewModel.togglePlayPause()
                testDispatcher.scheduler.advanceUntilIdle()
                val firstToggle = viewModel.playerState.value.isPlaying
                
                viewModel.togglePlayPause()
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playerState.value.isPlaying shouldBe !firstToggle
            }
        }
        
        describe("seekTo") {
            it("should update position and time") {
                val position = 0.5f // 50%
                
                viewModel.seekTo(position)
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playerState.value.currentPosition shouldBe position
                viewModel.playerState.value.currentTimeMs shouldBe (position * viewModel.playerState.value.durationMs).toLong()
            }
            
            it("should handle edge cases") {
                viewModel.seekTo(0f)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.playerState.value.currentPosition shouldBe 0f
                
                viewModel.seekTo(1f)
                testDispatcher.scheduler.advanceUntilIdle()
                viewModel.playerState.value.currentPosition shouldBe 1f
            }
        }
        
        describe("playNext") {
            it("should reset position") {
                viewModel.seekTo(0.5f)
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playNext()
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playerState.value.currentPosition shouldBe 0f
                viewModel.playerState.value.currentTimeMs shouldBe 0L
            }
        }
        
        describe("playPrevious") {
            it("should reset position") {
                viewModel.seekTo(0.7f)
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playPrevious()
                testDispatcher.scheduler.advanceUntilIdle()
                
                viewModel.playerState.value.currentPosition shouldBe 0f
                viewModel.playerState.value.currentTimeMs shouldBe 0L
            }
        }
        
        describe("state flow") {
            it("should emit state changes") {
                viewModel.playerState.test {
                    val initialState = awaitItem()
                    initialState.currentSong shouldBe null
                    
                    val song = Song("Flow Test", "Artist", "image.jpg")
                    viewModel.playSong(song)
                    testDispatcher.scheduler.advanceUntilIdle()
                    
                    val newState = awaitItem()
                    newState.currentSong shouldNotBe null
                    newState.isPlaying shouldBe true
                    
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }
})
