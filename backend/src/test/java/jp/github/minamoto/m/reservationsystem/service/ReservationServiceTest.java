package jp.github.minamoto.m.reservationsystem.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import jp.github.minamoto.m.reservationsystem.domain.TimeSlotStatus;
import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDTO;
import jp.github.minamoto.m.reservationsystem.dto.ReservationResponseDTO;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;
import jp.github.minamoto.m.reservationsystem.entity.TimeSlot;
import jp.github.minamoto.m.reservationsystem.repository.ReservationRepository;
import jp.github.minamoto.m.reservationsystem.repository.TimeSlotRepository;
import jp.github.minamoto.m.reservationsystem.service.exception.ReservationNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private TimeSlotRepository timeSlotRepository;
	
    @InjectMocks
    private ReservationService reservationService;

    @Test
    void create_TimeSlotOpen_success() {
        // Given: 空きの予約枠（OPEN）と予約作成リクエストが存在する
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setDate(LocalDate.of(2025, 2, 10));
        timeSlot.setStartTime(LocalTime.of(9, 0));
        timeSlot.setEndTime(LocalTime.of(9, 30));
        timeSlot.setStatus(TimeSlotStatus.OPEN);

        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));

        ReservationCreateRequestDTO dto = new ReservationCreateRequestDTO();
        dto.setTimeSlotId(1L);
        dto.setName("テストユーザー");
        dto.setPhoneNumber("09012345678");

        Reservation savedReservation = new Reservation();
        savedReservation.setId(1L);
        savedReservation.setTimeSlot(timeSlot);
        savedReservation.setStatus(ReservationStatus.CONFIRMED);
        savedReservation.setName(dto.getName());
        savedReservation.setPhoneNumber(dto.getPhoneNumber());

        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        // When: 予約を作成する
        ReservationResponseDTO result = reservationService.create(dto);

        // Then: 予約が作成され、レスポンスに正しい内容が含まれる。予約枠は CLOSED になる
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getTimeSlotId()).isEqualTo(1L);
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2025, 2, 10));
        assertThat(result.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(result.getEndTime()).isEqualTo(LocalTime.of(9, 30));
        assertThat(result.getStatus()).isEqualTo("CONFIRMED");

        assertThat(timeSlot.getStatus()).isEqualTo(TimeSlotStatus.CLOSED);

        verify(reservationRepository).save(any(Reservation.class));
        verify(timeSlotRepository).findById(1L);
    }

    @Test
    void cancel_confirmedReservationExists_returnsCanceledReservation() {
    	// Given: ステータスが予約済みの予約が存在する
    	Long reservationId = 1L;

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStatus(TimeSlotStatus.CLOSED);
    	
        Reservation reservation = new Reservation();
    	reservation.setId(reservationId);
    	reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setTimeSlot(timeSlot);
    	
    	when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
    	
    	// When: 予約をキャンセルする
    	reservationService.cancel(reservationId);
    	
    	// Then: 予約のステータスがキャンセルされている
    	assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
    	
    	verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void create_TimeSlotClosed_throwsIllegalArgumentException() {
        // Given: 予約枠が予約済み（CLOSED）であり、その枠で予約作成リクエストを送る
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setDate(LocalDate.of(2025, 2, 10));
        timeSlot.setStartTime(LocalTime.of(9, 0));
        timeSlot.setEndTime(LocalTime.of(9, 30));
        timeSlot.setStatus(TimeSlotStatus.CLOSED);

        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));

        ReservationCreateRequestDTO dto = new ReservationCreateRequestDTO();
        dto.setTimeSlotId(1L);
        dto.setName("テストユーザー");
        dto.setPhoneNumber("09012345678");

        // When: 予約を作成する
        // Then: IllegalArgumentException がスローされ、予約は保存されない
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.create(dto);
        });

        verify(timeSlotRepository).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
    
    @Test
    void findAll_hasTwoReservations_returnsTwoDtos() {
        // Given: 2件の予約が存在する
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setId(100L);
        timeSlot1.setDate(LocalDate.of(2025, 2, 10));
        timeSlot1.setStartTime(LocalTime.of(9, 0));
        timeSlot1.setEndTime(LocalTime.of(9, 30));
    
        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setName("ユーザー1");
        r1.setStatus(ReservationStatus.CONFIRMED);
        r1.setTimeSlot(timeSlot1);
    
        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setId(200L);
        timeSlot2.setDate(LocalDate.of(2025, 2, 11));
        timeSlot2.setStartTime(LocalTime.of(10, 0));
        timeSlot2.setEndTime(LocalTime.of(10, 30));
    
        Reservation r2 = new Reservation();
        r2.setId(2L);
        r2.setName("ユーザー2");
        r2.setStatus(ReservationStatus.CANCELED);
        r2.setTimeSlot(timeSlot2);
    
        when(reservationRepository.findAll()).thenReturn(List.of(r1, r2));
    
        // When: 予約一覧を取得する
        List<ReservationResponseDTO> result = reservationService.findAll();
    
        // Then: 2件の予約がDTOに変換されて返る
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("ユーザー1");
        assertThat(result.get(1).getStatus()).isEqualTo("CANCELED"); // ★ タイポ修正
    
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void findAllConfirmed_twoConfirmedReservationsExist_returnsTwoConfirmedDtos() {
        // Given: CONFIRMED の予約が2件存在する
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setId(100L);
        timeSlot1.setDate(LocalDate.of(2025, 2, 10));
        timeSlot1.setStartTime(LocalTime.of(9, 0));
        timeSlot1.setEndTime(LocalTime.of(9, 30));
        timeSlot1.setStatus(TimeSlotStatus.CLOSED);

        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setName("ユーザー1");
        r1.setStatus(ReservationStatus.CONFIRMED);
        r1.setTimeSlot(timeSlot1);

        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setId(200L);
        timeSlot2.setDate(LocalDate.of(2025, 2, 11));
        timeSlot2.setStartTime(LocalTime.of(10, 0));
        timeSlot2.setEndTime(LocalTime.of(10, 30));
        timeSlot2.setStatus(TimeSlotStatus.CLOSED);

		 Reservation r2 = new Reservation();
	     r2.setId(2L);
	     r2.setName("ユーザー2");
	     r2.setStatus(ReservationStatus.CONFIRMED);
	     r2.setTimeSlot(timeSlot2);
         timeSlot2.setStatus(TimeSlotStatus.CLOSED);
	     
	     when(reservationRepository.findByStatus(ReservationStatus.CONFIRMED))
	    		 .thenReturn(List.of(r1, r2));
	     
	    // When: 予約済み一覧を取得する
	     List<ReservationResponseDTO> result = reservationService.findAllConfirmed();
	     
        // Then: CONFIRMED の2件がDTOとして返る
	     assertThat(result).hasSize(2);
	     assertThat(result.get(0).getStatus()).isEqualTo("CONFIRMED");
	     assertThat(result.get(1).getStatus()).isEqualTo("CONFIRMED");
	     
	     verify(reservationRepository, times(1)).findByStatus(ReservationStatus.CONFIRMED);
    }

    @Test
    void findById_reservationExists_returnsReservationDto() {
    	// Given: 予約IDが1の予約が存在する
    	Long reservationId = 1L;
    	
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(100L);
        timeSlot.setDate(LocalDate.of(2025, 2, 10));
        timeSlot.setStartTime(LocalTime.of(9, 0));
        timeSlot.setEndTime(LocalTime.of(9, 30));
        timeSlot.setStatus(TimeSlotStatus.CLOSED);

    	Reservation reservation = new Reservation();
		reservation.setId(reservationId);
		reservation.setName("ユーザー1");
		reservation.setStatus(ReservationStatus.CONFIRMED);
		reservation.setTimeSlot(timeSlot);
		
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
		
		// When: 予約情報を取得する
		ReservationResponseDTO result =  reservationService.findById(reservationId);
		
		// Then: 予約情報が取得でき、DTOに変換されて返る
		assertThat(result.getName()).isEqualTo("ユーザー1");
	    assertThat(result.getStatus()).isEqualTo("CONFIRMED");
	    
	    verify(reservationRepository, times(1)).findById(reservationId);
        // ※timeSlotはダーティチェックされるため、verify(timeSlotRepository)は不要
    }

    @Test
    void findById_reservationNotExists_throwsReservationNotFoundException() {
    	// Given: 存在しない予約IDを指定する
    	Long reservationId = 999L;
		
		when(reservationRepository.findById(reservationId))
            .thenReturn(Optional.empty());
		
		// When: 予約情報を取得する
		// Then: ReservationNotFoundExceptionがスローされる
		assertThrows(ReservationNotFoundException.class, () -> {
			reservationService.findById(reservationId);
		});	
		
		verify(reservationRepository, times(1)).findById(reservationId);
    }
}