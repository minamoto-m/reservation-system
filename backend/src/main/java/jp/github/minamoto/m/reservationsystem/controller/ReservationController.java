package jp.github.minamoto.m.reservationsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jp.github.minamoto.m.reservationsystem.dto.ReservationCancelResponseDTO;
import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDTO;
import jp.github.minamoto.m.reservationsystem.dto.ReservationResponseDTO;
import jp.github.minamoto.m.reservationsystem.service.ReservationService;

@RestController
@RequestMapping("/v1/reservations")
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
	public ReservationResponseDTO create(@RequestBody ReservationCreateRequestDTO dto) {
		return reservationService.create(dto);
	}
	
	/*
	 * 予約IDを指定して予約をキャンセルする。
	 * 
	 * @param reservationId 予約ID
	 * @return キャンセルされた予約情報
	 */
	@PatchMapping("/{reservationId}/cancel")
	@ResponseStatus(HttpStatus.OK)
	public ReservationCancelResponseDTO cancel(@PathVariable Long reservationId) {
		return reservationService.cancel(reservationId);
	}
	
	/*
	 * 予約済みの予約一覧を取得する。
	 * 
	 * @return 予約レスポンスDTOのリスト
	 */
	@GetMapping
	public List<ReservationResponseDTO> findAllConfirmed() {
		return reservationService.findAllConfirmed();
	}
	
	/*
	 * 予約IDを指定して予約情報を取得する。
	 * 
	 * @param reservationId 予約ID
	 * @return 予約レスポンスDTO
	 */
	@GetMapping("/{reservationId}")
	public ReservationResponseDTO findById(@PathVariable Long reservationId) {
		return reservationService.findById(reservationId);
	}
}
