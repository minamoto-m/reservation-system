package jp.github.minamoto.m.reservationsystem.controller.advice;

import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
        DataIntegrityViolationException e
    ) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of(
                "error", "TIME_SLOT_ALREADY_TAKEN",
                "message", "予約枠がすでに埋まっています"
            ));
    }
}
