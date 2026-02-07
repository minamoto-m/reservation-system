package jp.github.minamoto.m.reservationsystem.controller.advice;

import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jp.github.minamoto.m.reservationsystem.service.exception.ReservationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ReservationNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ReservationNotFoundException ex) {
        return Map.of(
            "error", "RESERVATION_NOT_FOUND",
            "message", ex.getMessage()
        );
    }
}
