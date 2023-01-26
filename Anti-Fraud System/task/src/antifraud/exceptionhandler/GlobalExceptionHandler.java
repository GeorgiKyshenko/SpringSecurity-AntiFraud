package antifraud.exceptionhandler;

import antifraud.errors.*;
import antifraud.errors.DTO.ErrorDTO;
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

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(ExistingUserException exception) {
        log.info("User already exists user exception");
        return ResponseEntity.status(409)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(UserNotFoundException exception) {
        log.info("User Not Found user exception");
        return ResponseEntity.status(404)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(NotViableRoleException exception) {
        log.info("Role is not SUPPORT/MERCHANT exception");
        return ResponseEntity.status(400)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(IllegalRoleUpdateException exception) {
        log.info("User is already assign to this role exception");
        return ResponseEntity.status(409)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(IllegalActionException exception) {
        log.info("Unauthorized actions against ADMINISTRATOR user exception");
        return ResponseEntity.status(404)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(IpDuplicateException exception) {
        log.info("Duplicate IP exception");
        return ResponseEntity.status(409)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(IpNotFoundException exception) {
        log.info("IP not found in the database exception");
        return ResponseEntity.status(404)
                .body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorDTO> errorHandler(IncorrectIpInput exception) {
        log.info("Wrong IP format exception");
        return ResponseEntity.status(400)
                .body(new ErrorDTO(exception.getMessage()));
    }
}
