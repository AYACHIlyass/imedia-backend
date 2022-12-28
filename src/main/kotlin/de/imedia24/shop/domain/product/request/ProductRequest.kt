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
    val description: String = "",
    @field:Positive(message = "please provide a positive number for the price")
    val price: BigDecimal,
) {
    open fun toProductEntity() = ProductEntity(
        sku = sku,
        name = name,
        description = description,
        price = price,
    )

    override fun toString(): String {
        return "ProductRequest(sku='$sku', name='$name', description='$description', price=$price)"
    }

    override fun equals(other: Any?): Boolean {
        if (other === null || other !is ProductRequest) return false
        else if(other === this) return true
        else if (sku != other.sku) return false
        else if (name != other.name) return false
        else if (description != other.description) return false
        else if (price != other.price) return false
        return true
    }

}
