package de.imedia24.shop.exceptionManagement

import de.imedia24.shop.exceptionManagement.model.Error
import de.imedia24.shop.exceptionManagement.model.SubError
import de.imedia24.shop.service.exceptions.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun productNotFoundExceptionHandler(productNotFoundException: ProductNotFoundException): ResponseEntity<Error> {
        val error = Error(message = productNotFoundException.errorMessage, httpStatus = HttpStatus.NOT_FOUND.name)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun parameterIsNotValid(constraintViolationException: ConstraintViolationException): ResponseEntity<Error> {
        val details = constraintViolationException.constraintViolations.map { constraintViolation ->
            SubError(
                constraintViolation.propertyPath.toString(),
                constraintViolation.invalidValue.toString(),
                constraintViolation.message
            )
        }
        return ResponseEntity(
            Error(
                message = constraintViolationException.message.toString(),
                httpStatus = HttpStatus.BAD_REQUEST.name,
                details = details
            ), HttpStatus.BAD_REQUEST
        )
    }
}