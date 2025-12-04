package com.example.teamusic_grupo11.network

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * Suite de tests para NetworkResult.
 * 
 * NetworkResult es una clase sealed que representa el resultado de una operación de red.
 * Puede ser uno de tres estados:
 * - Success: La operación fue exitosa y contiene datos
 * - Error: La operación falló y contiene un mensaje de error
 * - Loading: La operación está en progreso
 * 
 * Este patrón es útil para manejar estados de UI de forma segura y predecible.
 */
class NetworkResultTest : DescribeSpec({
    
    describe("NetworkResult") {
        
        describe("Success") {
            it("debería contener datos") {
                // ===== GIVEN =====
                // Paso 1: Crear datos de prueba
                val data = "test data"
                
                // ===== WHEN =====
                // Paso 2: Crear un NetworkResult.Success con los datos
                val result = NetworkResult.Success(data)
                
                // ===== THEN =====
                // Paso 3: Verificar que es del tipo correcto
                result.shouldBeInstanceOf<NetworkResult.Success<String>>()
                // Paso 4: Verificar que los datos se almacenaron correctamente
                result.data shouldBe data
            }
        }
        
        describe("Error") {
            it("debería contener mensaje de error") {
                // ===== GIVEN =====
                // Paso 1: Crear un mensaje de error
                val message = "Error occurred"
                
                // ===== WHEN =====
                // Paso 2: Crear un NetworkResult.Error solo con mensaje
                val result = NetworkResult.Error(message)
                
                // ===== THEN =====
                // Paso 3: Verificar que es del tipo correcto
                result.shouldBeInstanceOf<NetworkResult.Error>()
                // Paso 4: Verificar que el mensaje se almacenó correctamente
                result.message shouldBe message
                // Paso 5: Verificar que el código es null (no se proporcionó)
                result.code shouldBe null
            }
            
            it("debería contener mensaje de error y código") {
                // ===== GIVEN =====
                // Paso 1: Crear mensaje y código de error HTTP
                val message = "Not found"
                val code = 404
                
                // ===== WHEN =====
                // Paso 2: Crear un NetworkResult.Error con mensaje y código
                val result = NetworkResult.Error(message, code)
                
                // ===== THEN =====
                // Paso 3: Verificar el tipo
                result.shouldBeInstanceOf<NetworkResult.Error>()
                // Paso 4: Verificar que el mensaje se almacenó
                result.message shouldBe message
                // Paso 5: Verificar que el código HTTP se almacenó
                result.code shouldBe code
            }
        }
        
        describe("Loading") {
            it("debería ser un singleton") {
                // ===== WHEN =====
                // Paso 1: Obtener dos referencias a Loading
                val result1 = NetworkResult.Loading
                val result2 = NetworkResult.Loading
                
                // ===== THEN =====
                // Paso 2: Verificar que es del tipo correcto
                result1.shouldBeInstanceOf<NetworkResult.Loading>()
                // Paso 3: Verificar que ambas referencias apuntan al mismo objeto
                // (Loading es un objeto singleton, no una clase)
                result1 shouldBe result2
            }
        }
        
        describe("type safety") {
            it("debería mantener seguridad de tipos para Success") {
                // Este test demuestra que NetworkResult mantiene la seguridad de tipos
                // Es decir, si creamos un Success<TestData>, solo podemos acceder a TestData
                
                // ===== GIVEN =====
                // Paso 1: Definir una clase de datos personalizada
                data class TestData(val value: String)
                val testData = TestData("test")
                
                // ===== WHEN =====
                // Paso 2: Crear un NetworkResult con tipo específico
                val result: NetworkResult<TestData> = NetworkResult.Success(testData)
                
                // ===== THEN =====
                // Paso 3: Verificar el tipo
                result.shouldBeInstanceOf<NetworkResult.Success<TestData>>()
                // Paso 4: Verificar que podemos acceder a las propiedades del tipo
                (result as NetworkResult.Success).data.value shouldBe "test"
            }
        }
    }
})
