package com.documentconverter.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(exception: IllegalStateException): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            error = "CONVERSION_FAILED",
            message = exception.message ?: "Conversion failed"
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            error = "INTERNAL_ERROR",
            message = "Unexpected error"
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}
