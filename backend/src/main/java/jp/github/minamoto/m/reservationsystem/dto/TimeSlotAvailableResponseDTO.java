package jp.github.minamoto.m.reservationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotAvailableResponseDTO {
    private Long timeSlotId;
    private String startTime;  // "HH:mm" 形式
}
