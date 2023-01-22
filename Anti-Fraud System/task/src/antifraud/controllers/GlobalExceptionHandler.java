package antifraud.controllers;

import antifraud.errors.BusinessException;
import antifraud.errors.DTO.ErrorDTO;
import antifraud.errors.NegativeNumberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(BusinessException exception) {
        log.info("Amount in letters exception");
        return ResponseEntity.badRequest()
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(NegativeNumberException exception) {
        log.info("Negative number exception");
        return ResponseEntity.badRequest()
                .body(new ErrorDTO(exception.getMessage()));
    }
}
