package de.imedia24.shop

import de.imedia24.shop.domain.product.request.ProductRequest
import de.imedia24.shop.domain.product.response.ProductResponse
import java.math.BigDecimal

var products = ArrayList<ProductResponse>();

var productResponse = ProductResponse(
    "2",
    "New T-shirt Nike",
    "This T-shirt is amazing",
    BigDecimal.valueOf(60),
    BigDecimal.valueOf(0)
)
var productRequest = ProductRequest(
    "2",
    "New T-shirt Nike",
    "This T-shirt is amazing",
    BigDecimal.valueOf(60),
)
var invalidProductRequest = ProductRequest(
    "invalid",
    "",
    "This T-shirt is amazing",
    BigDecimal.valueOf(-60),
)