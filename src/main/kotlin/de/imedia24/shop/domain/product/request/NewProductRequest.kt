package de.imedia24.shop.domain.product.request

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal
import javax.validation.constraints.Positive

class NewProductRequest(
    sku: String,
    name: String,
    description: String = "",
    price: BigDecimal,
    @field:Positive(message = "please provide a positive number for the price")
    val stock: BigDecimal,

    ) : ProductRequest(sku, name, description, price) {
    override fun toProductEntity(): ProductEntity = ProductEntity(
        sku = sku,
        name = name,
        description = description,
        price = price,
        stock = stock
    )

    override fun toString(): String {
        return "NewProductRequest(sku='$sku', name='$name', description='$description', price=$price stock=$stock)"
    }


}