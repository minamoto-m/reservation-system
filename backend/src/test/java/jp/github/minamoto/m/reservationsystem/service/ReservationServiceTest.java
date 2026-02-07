package jp.github.minamoto.m.reservationsystem.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDto;
import jp.github.minamoto.m.reservationsystem.dto.ReservationResponseDto;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;
import jp.github.minamoto.m.reservationsystem.repository.ReservationRepository;
import jp.github.minamoto.m.reservationsystem.service.exception.ReservationNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

	@Mock
	private ReservationRepository reservationRepository;
	
    @InjectMocks
    private ReservationService reservationService;

    /*
     * テスト対象：create 正常系<br>
     * 入力値：なし<br>
     * 期待結果：<br>
     * <ul>
     *   <li>EntityのListがReservationResponseDtoのListに正しく変換されていること</li>
     *   <li>返却されるListのサイズが正しいこと</li>
     *   <li>reservationRepository.findAllが1回呼ばれていること</li>
     * </ul>
     */
    @Test
    void create_succes() {
    	// 前提
        ReservationCreateRequestDto dto = new ReservationCreateRequestDto();
        dto.setReservationDate(LocalDate.of(2026, 1, 24));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(9, 30));
        dto.setName("テストユーザー");
        dto.setPhoneNumber("09012345678");
        
        Reservation saved = new Reservation();
        saved.setId(1L);
        saved.setName(dto.getName());
        saved.setStartTime(dto.getStartTime());
        
        when(reservationRepository.save(any(Reservation.class)))
        	.thenReturn(saved);

        // 実行
        Reservation result = reservationService.create(dto);

        // 検証
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getStartTime()).isEqualTo(dto.getStartTime());
        
        verify(reservationRepository).save(any(Reservation.class));
    }
    
    /*
     * テスト対象：findAll 正常系<br>
     * 入力値：なし<br>
     * 期待結果：<br>
     * <ul>
     *   <li>EntityのListがReservationResponseDtoのListに正しく変換されていること</li>
     *   <li>返却されるListのサイズが正しいこと</li>
     *   <li>reservationRepository.findAllが1回呼ばれていること</li>
     * </ul>
     */
    @Test
    void findAll_success() {
    	// 前提
		 Reservation r1 = new Reservation();
	     r1.setId(1L);
	     r1.setName("ユーザー1");
	     r1.setStatus(ReservationStatus.ACTIVE);
	     
		 Reservation r2 = new Reservation();
	     r2.setId(2L);
	     r2.setName("ユーザー2");
	     r2.setStatus(ReservationStatus.CANCELLED);
	     
	     when(reservationRepository.findAll()).thenReturn(List.of(r1, r2));
	     
	     // 実行
	     List<ReservationResponseDto> result = reservationService.findAll();
	     
	     // 検証
	     assertThat(result).hasSize(2);
	     assertThat(result.get(0).getName()).isEqualTo("ユーザー1");
	     assertThat(result.get(1).getStatus()).isEqualTo("CANCELLED");
	     
	     verify(reservationRepository, times(1)).findAll();
    }
    
    /*
     * テスト対象：findAllActive 正常系<br>
     * 入力値：なし<br>
     * 期待結果：<br>
     * <ul>
     *   <li>EntityのListがReservationResponseDtoのListに正しく変換されていること</li>
     *   <li>返却されるListのサイズが正しいこと</li>
     *   <li>reservationRepository.findByStatusが1回呼ばれていること</li>
     * </ul>
     */
    @Test
    void findAllActive() {
    	// 前提
		 Reservation r1 = new Reservation();
	     r1.setId(1L);
	     r1.setName("ユーザー1");
	     r1.setStatus(ReservationStatus.ACTIVE);
	     
		 Reservation r2 = new Reservation();
	     r2.setId(2L);
	     r2.setName("ユーザー2");
	     r2.setStatus(ReservationStatus.ACTIVE);
	     
	     when(reservationRepository.findByStatus(ReservationStatus.ACTIVE))
	    		 .thenReturn(List.of(r1, r2));
	     
	     // 実行
	     List<ReservationResponseDto> result = reservationService.findAllActive();
	     
	     // 検証
	     assertThat(result).hasSize(2);
	     assertThat(result.get(0).getStatus()).isEqualTo("ACTIVE");
	     assertThat(result.get(1).getStatus()).isEqualTo("ACTIVE");
	     
	     verify(reservationRepository, times(1)).findByStatus(ReservationStatus.ACTIVE);
    }
    
    /*
     * テスト対象：findById 正常系<br>
     * 入力値：id（Long）<br>
     * 期待結果：<br>
     * <ul>
     *   <li>指定したidの予約情報が取得できること</li>
     *   <li>EntityからResponseDtoへの変換が正しいこと</li>
     *   <li>reservationRepository.findById(id) が 1回呼ばれていること</li>
     * </ul>
     */
    @Test
    void findById_success() {
    	// 入力値
    	Long id = 1L;
    	
    	// 前提
    	Reservation reservation = new Reservation();
		reservation.setId(1L);
		reservation.setName("ユーザー1");
		reservation.setStatus(ReservationStatus.ACTIVE);
		
		when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
		
		// 実行
		ReservationResponseDto result =  reservationService.findById(id);
		
		// 検証
		assertThat(result.getName()).isEqualTo("ユーザー1");
	    assertThat(result.getStatus()).isEqualTo("ACTIVE");
	    
	    verify(reservationRepository, times(1)).findById(id);
    }
    
    /*
     * テスト対象：findById 異常系<br>
     * 入力値：存在しないid<br>
     * 期待結果：<br>
     * <p>例外としてReservationNotFoundExceptionが投げられること。</p>
     */
    @Test
    void findById_failed() {
    	// 入力値
    	Long id = 2L;
    	
    	// 前提
    	Reservation reservation = new Reservation();
		reservation.setId(1L);
		
		when(reservationRepository.findById(id)).thenReturn(Optional.empty());
		
		// 実行・検証
		assertThrows(ReservationNotFoundException.class, () -> {
			reservationService.findById(id);
		});
		
		verify(reservationRepository, times(1)).findById(id);
    }
    

    /*
     * テスト対象：cancel 正常系<br>
     * 入力値：id（Long）<br>
     * 期待結果：<br>
     * <ul>
     *   <li>statusがCANCELLEDになっていること</li>
     *   <li>reservationRepository.findById(id) が 1回呼ばれていること</li>
     *   <li>reservationRepository.save(reservation) が 1回呼ばれていること</li>
     * </ul>
     */
    @Test
    void cancel() {
    	// 入力値
    	Long id = 1L;
    	
    	// 前提
    	Reservation reservation = new Reservation();
    	reservation.setId(1L);
    	reservation.setStatus(ReservationStatus.ACTIVE);
    	
    	when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
    	
    	// 実行
    	reservationService.cancel(id);
    	
    	// 検証
    	assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    	
    	verify(reservationRepository, times(1)).findById(id);
    	verify(reservationRepository, times(1)).save(reservation);	
    }
}