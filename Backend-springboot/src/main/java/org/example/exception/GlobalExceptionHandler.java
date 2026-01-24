package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ========= USER ========= */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {

        log.warn("❌ USER_NOT_FOUND – {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "USER_NOT_FOUND",
                        ex.getMessage()
                ));
    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> HttpMediaTypehandle(HttpMediaTypeNotAcceptableException e){
        Throwable rootCause = e.getRootCause();
        String debugMessage = e.getMessage();

        if (rootCause != null) {
            debugMessage += " | Chi tiết: " + rootCause.getMessage();
        }

        log.error("❌ HttpMediaTypeNotAcceptableException: {}", debugMessage);

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new ErrorResponse(
                        "RESPONSE_FORMAT_ERROR: ",
                        "Server cannot convert to JSON, please check DTO: " + debugMessage
                ));
    };
    /* ========= FALLBACK ========= */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        log.error("❌ INTERNAL_SERVER_ERROR", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_ERROR",
                        "Unexpected error occurred"
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNoResourceFound(
            NoResourceFoundException e
    ){
        String detail = "❌ NoResourceFoundException: No API named " + e.getResourcePath();
        log.warn(detail);
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(
                  "METHOD_NOT_ALLOWED",
                        detail
                ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerMethodNotSupport(
            HttpRequestMethodNotSupportedException e
    ){
        String detail = "⚠\uFE0F HttpRequestMethodNotSupportedException: " + e.getMessage()
                + ", Support methods: " + e.getSupportedHttpMethods();

        log.warn(detail);

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(
                        "METHOD_NOT_ALLOWED",
                        detail
                ));
    }
}
