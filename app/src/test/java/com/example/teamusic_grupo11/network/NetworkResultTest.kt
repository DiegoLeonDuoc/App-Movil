package com.example.teamusic_grupo11.network

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class NetworkResultTest : DescribeSpec({
    
    describe("NetworkResult") {
        
        describe("Success") {
            it("should hold data") {
                // Given
                val data = "test data"
                
                // When
                val result = NetworkResult.Success(data)
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Success<String>>()
                result.data shouldBe data
            }
        }
        
        describe("Error") {
            it("should hold error message") {
                // Given
                val message = "Error occurred"
                
                // When
                val result = NetworkResult.Error(message)
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Error>()
                result.message shouldBe message
                result.code shouldBe null
            }
            
            it("should hold error message and code") {
                // Given
                val message = "Not found"
                val code = 404
                
                // When
                val result = NetworkResult.Error(message, code)
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Error>()
                result.message shouldBe message
                result.code shouldBe code
            }
        }
        
        describe("Loading") {
            it("should be a singleton") {
                // When
                val result1 = NetworkResult.Loading
                val result2 = NetworkResult.Loading
                
                // Then
                result1.shouldBeInstanceOf<NetworkResult.Loading>()
                result1 shouldBe result2
            }
        }
        
        describe("type safety") {
            it("should maintain type safety for Success") {
                // Given
                data class TestData(val value: String)
                val testData = TestData("test")
                
                // When
                val result: NetworkResult<TestData> = NetworkResult.Success(testData)
                
                // Then
                result.shouldBeInstanceOf<NetworkResult.Success<TestData>>()
                (result as NetworkResult.Success).data.value shouldBe "test"
            }
        }
    }
})
