package de.imedia24.shop.exceptionManagement

import de.imedia24.shop.exceptionManagement.model.Error
import de.imedia24.shop.exceptionManagement.model.SubError
import de.imedia24.shop.service.exceptions.ProductAlreadyExistException
import de.imedia24.shop.service.exceptions.ProductNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    private val logger = LoggerFactory.getLogger(ExceptionHandler::class.java)!!
    private val validationErrorMessage = "Validation error"

    @ExceptionHandler(Exception::class)
    fun genericExceptionHandler(exception: Exception): ResponseEntity<Error> {
        return createSimpleErrorResponseEntity(exception.message.toString(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
    @ExceptionHandler(ProductNotFoundException::class)
    fun productNotFoundExceptionHandler(productNotFoundException: ProductNotFoundException): ResponseEntity<Error> {
        return createSimpleErrorResponseEntity(productNotFoundException.errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun parameterIsNotValid(constraintViolationException: ConstraintViolationException): ResponseEntity<Error> {
        logger.error("a param is not valid ${constraintViolationException.message.toString()}")
        val details = constraintViolationException.constraintViolations.map { constraintViolation ->
            SubError(
                constraintViolation.propertyPath.toString(),
                constraintViolation.invalidValue.toString(),
                constraintViolation.message
            )
        }
        val error = Error(
            message = constraintViolationException.message.toString(),
            httpStatus = HttpStatus.BAD_REQUEST.name,
            details = details
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ProductAlreadyExistException::class)
    fun productAlreadyExistExceptionHandler(productAlreadyExistException: ProductAlreadyExistException): ResponseEntity<Error> {
        return createSimpleErrorResponseEntity(productAlreadyExistException.errorMessage, HttpStatus.BAD_REQUEST)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("argument is not valid : ${ex.message}")
        val details = ex.bindingResult.fieldErrors.map {
            SubError(
                it.field,
                it.rejectedValue.toString(),
                it.defaultMessage.toString()
            )
        }
        return ResponseEntity(
            Error(
                message = validationErrorMessage,
                httpStatus = status.name,
                details = details
            ), status
        )
    }


    private fun createSimpleErrorResponseEntity(message: String, status: HttpStatus): ResponseEntity<Error> {
        logger.error("error while calling an endpoint details: $message")
        val error = Error(message = message, httpStatus = status.name)
        return ResponseEntity(error, status)
    }
}