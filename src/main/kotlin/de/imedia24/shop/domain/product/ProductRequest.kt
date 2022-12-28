package de.imedia24.shop.domain.product

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class ProductRequest(
    @field:Pattern(regexp = "[1-9][0-9]*", message = "please provide a valid sku; positive number")
    val sku: String,
    @field:NotBlank(message = "please make sure that the name is not blank")
    val name: String,
    val description: String="",
    @field:Positive(message = "please provide a positive number for the price")
    val price: BigDecimal,
    @field:Positive(message = "please provide a positive number for the stock")
    val stock: BigDecimal,
    )
{
    fun toProductEntity() = ProductEntity(
        sku = sku,
        name = name,
        description = description ?: "",
        price = price,
        stock = stock
    )
}
