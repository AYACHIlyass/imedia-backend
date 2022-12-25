package de.imedia24.shop.service.exceptions

class ProductNotFoundException(val errorMessage: String) : RuntimeException(errorMessage) {
}