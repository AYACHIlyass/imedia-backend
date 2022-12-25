package de.imedia24.shop.exceptionManagement

import de.imedia24.shop.exceptionManagement.model.Error
import de.imedia24.shop.service.exceptions.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun productNotFoundExceptionHandler(productNotFoundException: ProductNotFoundException): ResponseEntity<Error> {
        val error = Error(message = productNotFoundException.errorMessage, httpStatus = HttpStatus.NOT_FOUND.name)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }
}