package jp.github.minamoto.m.reservationsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.github.minamoto.m.reservationsystem.domain.TimeSlotStatus;
import jp.github.minamoto.m.reservationsystem.dto.TimeSlotStatusResponseDTO;
import jp.github.minamoto.m.reservationsystem.service.TimeSlotService;

@RestController
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }
    
    /**
     * 予約枠を閉じる。
     * 
     * @param timeSlotId
     * @return 予約枠ステータスレスポンスDTO
     */
    @PutMapping("/admin/timeslots/{timeSlotId}/close")
    public ResponseEntity<TimeSlotStatusResponseDTO> close(@PathVariable Long timeSlotId) {
        return ResponseEntity.ok(timeSlotService.close(timeSlotId));
    }

    /**
     * 予約枠を開く。
     * 
     * @param timeSlotId
     * @return 予約枠ステータスレスポンスDTO
     */
    @PutMapping("/admin/timeslots/{timeSlotId}/open")
    public ResponseEntity<TimeSlotStatusResponseDTO> open(@PathVariable Long timeSlotId) {
        return ResponseEntity.ok(timeSlotService.open(timeSlotId));
    }


}
