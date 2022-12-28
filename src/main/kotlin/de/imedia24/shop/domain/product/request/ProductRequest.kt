package de.imedia24.shop.domain.product.request

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

open class ProductRequest(
    @field:Pattern(regexp = "[1-9][0-9]*", message = "please provide a valid sku; positive number")
    val sku: String,
    @field:NotBlank(message = "please make sure that the name is not blank")
    val name: String,
    val description: String="",
    @field:Positive(message = "please provide a positive number for the price")
    val price: BigDecimal,
    )
{
    open fun toProductEntity() = ProductEntity(
        sku = sku,
        name = name,
        description = description,
        price = price,
    )

    override fun toString(): String {
        return "ProductRequest(sku='$sku', name='$name', description='$description', price=$price)"
    }


}
