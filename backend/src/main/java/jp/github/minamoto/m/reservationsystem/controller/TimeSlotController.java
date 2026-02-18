package jp.github.minamoto.m.reservationsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.github.minamoto.m.reservationsystem.dto.TimeSlotAvailableResponseDTO;
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


    /**
     * 日付と医師IDを指定して空き予約枠（OPEN）を取得する。
     * クエリパラメータ: doctorId, date（yyyy-MM-dd）
     *
     * @param doctorId 医師ID（前ステップで選択した医師）
     * @param date 予約希望日（カレンダーで選択した日付）
     * @return 空き予約枠のDTOリスト
     */
    @GetMapping("/v1/timeslots")
    public ResponseEntity<List<TimeSlotAvailableResponseDTO>> findAvailable(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(timeSlotService.findAvailable(date, doctorId));
    }
}
