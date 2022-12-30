package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.request.NewProductRequest
import de.imedia24.shop.domain.product.request.ProductRequest
import de.imedia24.shop.domain.product.response.ProductResponse
import de.imedia24.shop.exceptionManagement.model.Error
import de.imedia24.shop.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Pattern

@RestController
@Validated
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/{sku}", produces = ["application/json;charset=utf-8"])
    @Operation(summary = "Get a product by its sku")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "product is found",
                content = [(Content(array = (ArraySchema(schema = Schema(implementation = ProductResponse::class)))))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "product not found",
                content = [Content(array = (ArraySchema(schema = Schema(implementation = Error::class))))]
            )]
    )
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")
        return ResponseEntity.ok(productService.findProductBySku(sku))
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "products are found",
                content = [(Content(array = (ArraySchema(schema = Schema(implementation = ProductResponse::class)))))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "products are not found",
                content = [Content(array = (ArraySchema(schema = Schema(implementation = Error::class))))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "skus is not valid",
                content = [Content(array = (ArraySchema(schema = Schema(implementation = Error::class))))]
            )
        ]
    )
    @Operation(summary = "Get a products by its skus")
    @GetMapping(produces = ["application/json;charset=utf-8"])
    fun findProductsBySkus(
        @Pattern(regexp = "[1-9][0-9]*(?:,[1-9][0-9]*)*", message = "skus must match the right pattern")
        @RequestParam("skus", required = true)
        skus: String
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Request for products $skus")
        return ResponseEntity.ok(productService.findProductBySkus(skus))
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "product has been added successfully",
                content = [(Content(array = (ArraySchema(schema = Schema(implementation = ProductResponse::class)))))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "product already exist or product information are not valid",
                content = [Content(array = (ArraySchema(schema = Schema(implementation = Error::class))))]
            ),
        ]
    )
    @Operation(summary = "add a product")
    @PostMapping(produces = ["application/json;charset=utf-8"])
    fun addProduct(@Valid @RequestBody newProductRequest: NewProductRequest): ResponseEntity<ProductResponse> {
        logger.info("add product $newProductRequest")
        return ResponseEntity.ok(productService.addProduct(newProductRequest))
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "product has been updated successfully",
                content = [(Content(array = (ArraySchema(schema = Schema(implementation = ProductResponse::class)))))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "product does not exist",
                content = [Content(array = (ArraySchema(schema = Schema(implementation = Error::class))))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "product information are not valid",
                content = [Content(array = (ArraySchema(schema = Schema(implementation = Error::class))))]
            )
        ]
    )
    @Operation(summary = "update a product")
    @PutMapping(produces = ["application/json;charset=utf-8"])
    fun updateProduct(@Valid @RequestBody productRequest: ProductRequest): ResponseEntity<ProductResponse> {
        logger.info("update product $productRequest")
        return ResponseEntity.ok(productService.updateProduct(productRequest))
    }
}
