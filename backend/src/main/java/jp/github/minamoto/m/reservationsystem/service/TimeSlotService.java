package jp.github.minamoto.m.reservationsystem.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.github.minamoto.m.reservationsystem.domain.TimeSlotStatus;
import jp.github.minamoto.m.reservationsystem.dto.TimeSlotStatusResponseDTO;
import jp.github.minamoto.m.reservationsystem.entity.TimeSlot;
import jp.github.minamoto.m.reservationsystem.repository.ReservationRepository;
import jp.github.minamoto.m.reservationsystem.repository.TimeSlotRepository;

@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final ReservationRepository reservationRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, ReservationRepository reservationRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * 予約枠をDOCTOR_UNAVAILABLEに更新する。
     * 
     * @param timeSlotId 予約枠ID
     * @throws IllegalArgumentException 予約枠が見つからない場合
     */
    @Transactional
    public TimeSlotStatusResponseDTO close(Long timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
            .orElseThrow(() -> new IllegalArgumentException("予約枠が見つかりません。"));
        
        timeSlot.setStatus(TimeSlotStatus.DOCTOR_UNAVAILABLE);

        return new TimeSlotStatusResponseDTO(timeSlotId, timeSlot.getStatus().name());
    }

    /**
     * 予約枠をOPENに更新する。
     * 
     * @param timeSlotId 予約枠ID
     * @throws IllegalArgumentException 予約枠が見つからない場合
     */
    @Transactional
    public TimeSlotStatusResponseDTO open(Long timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
            .orElseThrow(() -> new IllegalArgumentException("予約枠が見つかりません。"));

        // 予約が存在する場合はOPENに戻せない
        // TimeSlotからReservation方向にはリレーションがないため、ReservationRepositoryを使用して予約の存在を確認する
        if(reservationRepository.existsByTimeSlotId(timeSlotId)) {
            throw new IllegalArgumentException("予約が存在するためOPENに戻せません。");
        }
       
        timeSlot.setStatus(TimeSlotStatus.OPEN);

        return new TimeSlotStatusResponseDTO(timeSlotId, timeSlot.getStatus().name());
    }
}