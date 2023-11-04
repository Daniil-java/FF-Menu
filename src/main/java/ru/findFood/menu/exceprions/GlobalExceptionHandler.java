package ru.findFood.menu.exceprions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> catchNotFoundException(NotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> InnerRequestExceptionException(InnerRequestException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Sorry, something went wrong, we will fix it soon"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
