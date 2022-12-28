package de.imedia24.shop.db.repository

import de.imedia24.shop.db.entity.ProductEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ProductRepository : CrudRepository<ProductEntity, String> {

    fun findBySku(sku: String): ProductEntity?

    @Query("select p from ProductEntity p where p.sku in :skus")
    fun findBySkus(@Param("skus") skus: List<String>): List<ProductEntity>

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
        "update ProductEntity p set " +
                "p.description=:#{#updateProduct.description}," +
                "p.name=:#{#updateProduct.name}," +
                "p.price=:#{#updateProduct.price}," +
                "p.updatedAt=:#{#updateProduct.updatedAt}  where p.sku=:#{#updateProduct.sku}"
    )
    fun updateProduct(@Param("updateProduct") productEntity: ProductEntity): Int
}