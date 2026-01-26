package jp.github.minamoto.m.reservationsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDto;
import jp.github.minamoto.m.reservationsystem.dto.ReservationResponseDto;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;
import jp.github.minamoto.m.reservationsystem.service.ReservationService;

@RestController
@RequestMapping("reservations")
public class ReservationController {
	private final ReservationService reservationService;
	
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
	
	/*
	 * 予約を作成する。
	 * 
	 * @param 予約作成リクエストDTO
	 * @return 作成された予約情報
	 */
	@PostMapping
	public Reservation create(@RequestBody ReservationCreateRequestDto dto) {
		return reservationService.create(dto);
	}
	
	/*
	 * 予約一覧を取得する。
	 * 
	 * @return 予約一覧レスポンス
	 */
	@GetMapping
	public List<ReservationResponseDto> findAll() {
		return reservationService.findAll();
	}
	
	/*
	 * 指定されたIDの予約を1件取得する。
	 * 
	 * @param id 予約ID
	 * @return 予約情報のレスポンスDTO
	 */
	@GetMapping("/{id}")
	public ReservationResponseDto findById(@PathVariable Long id) {
		return reservationService.findById(id);
	}
}
