package com.sushistack.libproducer.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.stream.Collectors


@ControllerAdvice
class LibraryEventControllerAdvice: ResponseEntityExceptionHandler() {
    private var log = KotlinLogging.logger {}

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleRequestBody(ex: MethodArgumentNotValidException): ResponseEntity<*> {
        val errorList = ex.bindingResult.fieldErrors
        val errorMessage = errorList.stream()
            .map { fieldError: FieldError -> fieldError.field + " - " + fieldError.defaultMessage }
            .sorted()
            .collect(Collectors.joining(", "))
        log.info { "errorMessage : $errorMessage " }
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}