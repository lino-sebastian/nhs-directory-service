package com.national.health.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

/**
 * Controller Advice handling Application Exceptions
 */
@RestControllerAdvice
public class NHSDirectoryExceptionHandling {

    /*
     * Todo:Implement other exception handling and Custom Exceptions based on Exception hierarchy
     * */

    /**
     * Method handling generic exception
     *
     * @param exception :   exception
     * @return {@link ResponseEntity<ErrorMessage>}
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(Exception exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDate.now())
                .description(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
