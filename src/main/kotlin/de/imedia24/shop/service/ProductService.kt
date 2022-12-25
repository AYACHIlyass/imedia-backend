package de.imedia24.shop.service

import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.service.exceptions.ProductNotFoundException
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductResponse {
        return productRepository.findBySku(sku)?.toProductResponse()
            ?: throw ProductNotFoundException("product with sku : $sku can not be found. ")
    }

    fun findProductBySkus(skus: String): List<ProductResponse> {
        return productRepository.findBySkus(skus.split(",")).takeIf { it.isNotEmpty() }
            ?.let { it.map { productEntity -> productEntity.toProductResponse() } }
            ?: throw ProductNotFoundException("the products with skus = $skus are not found")
    }
}
