package de.imedia24.shop.service

import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.request.NewProductRequest
import de.imedia24.shop.domain.product.request.ProductRequest
import de.imedia24.shop.domain.product.response.ProductResponse
import de.imedia24.shop.domain.product.response.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.service.exceptions.ProductAlreadyExistException
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

    fun addProduct(newProductRequest: NewProductRequest): ProductResponse {
        return takeIf { !productRepository.existsById(newProductRequest.sku) }?.let {
            productRepository.save(
                newProductRequest.toProductEntity()
            ).toProductResponse()
        }
            ?: throw ProductAlreadyExistException(errorMessage = "the product with sku = ${newProductRequest.sku} already exist")
    }

    fun updateProduct(productRequest: ProductRequest): ProductResponse {
        return takeIf { productRepository.existsById(productRequest.sku) }?.let {
            productRepository.updateProduct(productRequest.toProductEntity())
            productRepository.findBySku(productRequest.sku)!!.toProductResponse()
        } ?: throw ProductNotFoundException("product with sku ${productRequest.sku} could not be found to update it")
    }
}
