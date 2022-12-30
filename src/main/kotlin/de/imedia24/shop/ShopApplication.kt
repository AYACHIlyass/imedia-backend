package de.imedia24.shop

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "Shop API", version = "1.0", description = "Shop Information"))
class ShopApplication

fun main(args: Array<String>) {
    runApplication<ShopApplication>(*args)
}
