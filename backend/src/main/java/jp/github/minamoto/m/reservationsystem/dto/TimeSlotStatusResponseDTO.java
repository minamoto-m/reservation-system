package jp.github.minamoto.m.reservationsystem.dto;

import lombok.Data;

@Data
public class TimeSlotStatusResponseDTO {
    private Long timeSlotId;
    private String status;

    public TimeSlotStatusResponseDTO(Long timeSlotId, String status) {
        this.timeSlotId = timeSlotId;
        this.status = status;
    }
}
