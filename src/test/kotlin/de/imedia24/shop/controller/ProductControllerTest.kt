package de.imedia24.shop.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.domain.product.response.ProductResponse
import de.imedia24.shop.invalidProductRequest
import de.imedia24.shop.productRequest
import de.imedia24.shop.productResponse
import de.imedia24.shop.products
import de.imedia24.shop.service.ProductService
import de.imedia24.shop.service.exceptions.ProductNotFoundException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.math.BigDecimal


@ExtendWith(SpringExtension::class)
@WebMvcTest(ProductController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProductControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    lateinit var productService: ProductService

    @Value("\${product.endpoint.url}")
    lateinit var endpointUrl: String

    @BeforeAll
    fun setUp() {
        products.add(
            ProductResponse(
                "1",
                "Coat",
                "This Coat is amazing",
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0)
            )
        )
        products.add(
            ProductResponse(
                "2",
                "T-shirt Nike",
                "This T-shirt is amazing",
                BigDecimal.valueOf(29.50),
                BigDecimal.valueOf(0)
            )
        )
    }

    @AfterAll
    fun cleanUp() {
        products.clear()
    }

    @Nested
    inner class FindProductsBySkus {

        @Test
        fun `find products by skus with invalid path parameter`() {
            val invalidSku = "1,"
            mockMvc.get("${endpointUrl}?skus=$invalidSku") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { HttpStatus.BAD_REQUEST }
                content {
                    json(
                        "{\n" +
                                "    \"message\": \"findProductsBySkus.skus: skus must match the right pattern\",\n" +
                                "    \"http_status\": \"BAD_REQUEST\",\n" +
                                "    \"details\": [\n" +
                                "        {\n" +
                                "            \"field\": \"findProductsBySkus.skus\",\n" +
                                "            \"rejected_value\": \"$invalidSku\",\n" +
                                "            \"message\": \"skus must match the right pattern\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}", false
                    )
                }
            }
        }

        @Test
        fun findProductsBySku() {
            val skus = "1,2,3"
            Mockito.`when`(productService.findProductBySkus(skus)).thenReturn(products)
            mockMvc.get("${endpointUrl}?skus=$skus") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { HttpStatus.OK }
                content {
                    json(
                        "[\n" +
                                "    {\n" +
                                "        \"sku\": \"1\",\n" +
                                "        \"name\": \"Coat\",\n" +
                                "        \"description\": \"This Coat is amazing\",\n" +
                                "        \"price\": 300,\n" +
                                "        \"stock\": 0\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"sku\": \"2\",\n" +
                                "        \"name\": \"T-shirt Nike\",\n" +
                                "        \"description\": \"This T-shirt is amazing\",\n" +
                                "        \"price\": 29.50,\n" +
                                "        \"stock\": 0\n" +
                                "    }\n" +
                                "]"
                    )
                }
            }
        }

        @Test
        fun `find products by skus that don't exist`() {
            val skus = "9,10"
            Mockito.`when`(productService.findProductBySkus(skus))
                .thenThrow(ProductNotFoundException("the products with skus = $skus are not found"))
            mockMvc.get("${endpointUrl}?skus=$skus") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { HttpStatus.NOT_FOUND }
                content {
                    json(
                        "{\n" +
                                "    \"message\": \"the products with skus = $skus are not found\",\n" +
                                "    \"http_status\": \"NOT_FOUND\"\n" +
                                "}", false
                    )
                }
            }
        }
    }

    @Nested
    inner class UpdateProducts {
        @Test
        fun updateProduct() {
            Mockito.`when`(productService.updateProduct(productRequest)).thenReturn(productResponse)
            mockMvc.put(endpointUrl) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = toJson(productRequest)
            }.andExpect {
                status { HttpStatus.OK }
                content {
                    json(
                        "{\n" +
                                "    \"sku\": \"2\",\n" +
                                "    \"name\": \"New T-shirt Nike\",\n" +
                                "    \"description\": \"This T-shirt is amazing\",\n" +
                                "    \"price\": 60,\n" +
                                "    \"stock\": 0\n" +
                                "}"
                    )
                }
            }
        }

        @Test
        fun `update product that does not exist`() {
            Mockito.`when`(productService.updateProduct(productRequest))
                .thenThrow(ProductNotFoundException("product with sku ${productRequest.sku} could not be found to update it"))
            mockMvc.put(endpointUrl) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = toJson(productRequest)
            }.andExpect {
                status { HttpStatus.NOT_FOUND }
                content {
                    json(
                        "{\n" +
                                "    \"message\": \"product with sku ${productRequest.sku} could not be found to update it\",\n" +
                                "    \"http_status\": \"NOT_FOUND\"\n" +
                                "}", false
                    )
                }
            }
        }

        @Test
        fun `update product with invalid information`() {
            mockMvc.put(endpointUrl) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = toJson(invalidProductRequest)
            }.andExpect {
                status { HttpStatus.BAD_REQUEST }
                content {
                    json(
                        "{\n" +
                                "    \"message\": \"Validation error\",\n" +
                                "    \"http_status\": \"BAD_REQUEST\",\n" +
                                "    \"details\": [\n" +
                                "        {\n" +
                                "            \"field\": \"sku\",\n" +
                                "            \"rejected_value\": \"invalid\",\n" +
                                "            \"message\": \"please provide a valid sku; positive number\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"field\": \"price\",\n" +
                                "            \"rejected_value\": \"-60\",\n" +
                                "            \"message\": \"please provide a positive number for the price\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"field\": \"name\",\n" +
                                "            \"rejected_value\": \"\",\n" +
                                "            \"message\": \"please make sure that the name is not blank\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}", false
                    )
                }
            }
        }
    }

    private fun toJson(productRequest: Any): String = ObjectMapper().writer().writeValueAsString(productRequest)
}