package jp.github.minamoto.m.reservationsystem.service.exception;

public class TimeSlotAlreadyTakenException extends RuntimeException {
    
    public TimeSlotAlreadyTakenException(String message) {
        super(message);
    }
}
