package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Pattern

@RestController
@Validated
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")
        return ResponseEntity.ok(productService.findProductBySku(sku))
    }

    @GetMapping(produces = ["application/json;charset=utf-8"])
    fun findProductsBySkus(
        @Pattern(regexp = "[1-9][0-9]*(?:,[1-9][0-9]*)*", message = "skus must match the right pattern")
        @RequestParam("skus", required = true)
        skus: String
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Request for products $skus")
        return ResponseEntity.ok(productService.findProductBySkus(skus))
    }
}
