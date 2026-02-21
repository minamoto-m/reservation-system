package jp.github.minamoto.m.reservationsystem.service.exception;

public class TimeSlotNotFoundException extends RuntimeException {
    
    public TimeSlotNotFoundException(Long id) {
        super("TimeSlot not found. id=" + id);
    }
}
