package jp.github.minamoto.m.reservationsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.github.minamoto.m.reservationsystem.domain.TimeSlotStatus;
import jp.github.minamoto.m.reservationsystem.dto.TimeSlotStatusResponseDTO;
import jp.github.minamoto.m.reservationsystem.entity.TimeSlot;
import jp.github.minamoto.m.reservationsystem.repository.ReservationRepository;
import jp.github.minamoto.m.reservationsystem.repository.TimeSlotRepository;

@ExtendWith(MockitoExtension.class)
public class TimeSlotServiceTest {
    
    @Mock
    private TimeSlotRepository timeSlotRepository;
    
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private TimeSlotService timeSlotService;

    @Test
    void close_timeSlotExists_returnsTimeSlotStatusResponseDTO() {
        
        // Given: ステータスがOPENの予約が存在する
        Long timeSlotId = 1L;

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStatus(TimeSlotStatus.OPEN);

        when(timeSlotRepository.findById(timeSlotId)).thenReturn(Optional.of(timeSlot));

        // When: ステータスを更新する
        TimeSlotStatusResponseDTO result = timeSlotService.close(timeSlotId);

        // Then: ステータスがDOCTOR_UNAVAILABLE に更新された DTO が返る
        assertThat(result.getTimeSlotId()).isEqualTo(timeSlotId);
        assertThat(result.getStatus()).isEqualTo(TimeSlotStatus.DOCTOR_UNAVAILABLE.name());
    }

    @Test
    void close_timeSlotNotExists_throwsIllegalArgumentException() {

        // Given: 予約枠が存在しない
        Long timeSlotId = 999L;
        when(timeSlotRepository.findById(timeSlotId)).thenReturn(Optional.empty());

        // When & Then: 予約枠が見つからないため例外がスローされる
        assertThrows(IllegalArgumentException.class, () -> timeSlotService.close(timeSlotId));
    }
    
    @Test
    void open_reservaitonNotExists_returnsTimeSlotStatusResponseDTO() {

        // Given: ステータスがRESERVEDの予約が存在する
        Long timeSlotId = 1L;

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStatus(TimeSlotStatus.RESERVED);

        when(timeSlotRepository.findById(timeSlotId)).thenReturn(Optional.of(timeSlot));

        // When: ステータスを更新する
        TimeSlotStatusResponseDTO result = timeSlotService.open(timeSlotId);

         // Then: ステータスが　OPEN に更新された DTO が返る
         assertThat(result.getTimeSlotId()).isEqualTo(timeSlotId);
         assertThat(result.getStatus()).isEqualTo(TimeSlotStatus.OPEN.name());
    }

    @Test
    void open_reservaitonExists_IllegalArgumentException() {

        // Given: 予約枠が存在し、その枠に予約が1件ある（existsByTimeSlotId が true）
        Long timeSlotId = 1L;

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStatus(TimeSlotStatus.RESERVED);

        when(timeSlotRepository.findById(timeSlotId)).thenReturn(Optional.of(timeSlot));
        when(reservationRepository.existsByTimeSlotId(timeSlotId)).thenReturn(true);

        // When & Then: 予約が存在するため例外がスローされる
        assertThrows(IllegalArgumentException.class, () -> timeSlotService.open(timeSlotId));
    }
}
