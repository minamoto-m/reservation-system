package jp.github.minamoto.m.reservationsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import jp.github.minamoto.m.reservationsystem.domain.TimeSlotStatus;
import jp.github.minamoto.m.reservationsystem.dto.ReservationCancelResponseDTO;
import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDTO;
import jp.github.minamoto.m.reservationsystem.dto.ReservationResponseDTO;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;
import jp.github.minamoto.m.reservationsystem.entity.TimeSlot;
import jp.github.minamoto.m.reservationsystem.repository.ReservationRepository;
import jp.github.minamoto.m.reservationsystem.repository.TimeSlotRepository;
import jp.github.minamoto.m.reservationsystem.service.exception.ReservationNotFoundException;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final TimeSlotRepository timeSlotRepository;

	public ReservationService(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository) {
		this.reservationRepository = reservationRepository;
		this.timeSlotRepository = timeSlotRepository;
	}

	/*
	 * 予約情報エンティティをレスポンス用DTOに変換する。
	 * 
	 * @param entity 予約情報エンティティ
	 * 
	 * @return 予約レスポンスDTO
	 */
	private ReservationResponseDTO toResponseDto(Reservation entity) {
		ReservationResponseDTO dto = new ReservationResponseDTO();

		dto.setReservationId(entity.getId());
		dto.setTimeSlotId(entity.getTimeSlot().getId());

		dto.setDate(entity.getTimeSlot().getDate());
		dto.setStartTime(entity.getTimeSlot().getStartTime());
		dto.setEndTime(entity.getTimeSlot().getEndTime());

		dto.setStatus(entity.getStatus().name());
		dto.setName(entity.getName());

		return dto;
	}

	/*
	 * 予約情報エンティティをキャンセルレスポンス用DTOに変換する。
	 * 
	 * @param entity 予約情報エンティティ
	 * 
	 * @return レスポンス用の予約情キャンセルレスポンスDTO
	 */
	private ReservationCancelResponseDTO toCancelResponseDTO(Reservation entity) {
		ReservationCancelResponseDTO dto = new ReservationCancelResponseDTO();

		dto.setReservationId(entity.getId());
		dto.setStatus(entity.getStatus().name());

		return dto;
	}

	/*
	 * 予約を作成する。
	 * 
	 * TimeSlotをロックして取得
	 * TimeSlotをRESERVEDに更新
	 * Reservationの作成
	 * 
	 * @param 予約作成リクエストDTO
	 * @return 作成された予約情報
	 */
	@Transactional
	public ReservationResponseDTO create(ReservationCreateRequestDTO dto) {

		// 予約枠を取得
		TimeSlot timeSlot = timeSlotRepository.findById(dto.getTimeSlotId())
			.orElseThrow(() -> new IllegalArgumentException("TimeSlot not found"));
		
		if(timeSlot.getStatus() != TimeSlotStatus.OPEN) {
			throw new IllegalArgumentException("すでに予約が存在しています。");
		}

		// 予約枠を予約済みに更新
		timeSlot.setStatus(TimeSlotStatus.RESERVED);

		// 予約情報の作成
		Reservation reservation = new Reservation();
		reservation.setTimeSlot(timeSlot);
		reservation.setStatus(ReservationStatus.CONFIRMED);
		reservation.setName(dto.getName());
		reservation.setPhoneNumber(dto.getPhoneNumber());

		Reservation savedReservation = reservationRepository.save(reservation);

		return toResponseDto(savedReservation);
	}

	/*
	 * 予約をキャンセルする。
	 * 
	 * <p>DBから予約データは削除せず、statusをCANCELEDに更新する。</p>
	 * 
	 * ReservationStatusがCONFIRMEDのもののみ対象
	 * ReservationStatusをCANCELEDに変更
	 * TimeSlotStatusをOPENに変更
	 * 
	 * @param reservationId 予約ID
	 * @return キャンセルされた予約情報
	 */
	@Transactional
	public ReservationCancelResponseDTO cancel(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ReservationNotFoundException(reservationId));

		if(reservation.getStatus() != ReservationStatus.CONFIRMED) {
			throw new IllegalArgumentException("予約をキャンセルできません。");
		}

		// 予約をキャンセル
		reservation.setStatus(ReservationStatus.CANCELED);

		// 予約枠を空きに更新
		TimeSlot timeSlot = reservation.getTimeSlot();
		timeSlot.setStatus(TimeSlotStatus.OPEN);

		return toCancelResponseDTO(reservation);
	}


	/*
	 * 予約一覧を取得する。
	 * 
	 * @return 予約レスポンスDTOのリスト
	 */
	public List<ReservationResponseDTO> findAll() {
		List<Reservation> reservations = reservationRepository.findAll();

		return reservations.stream().map(this::toResponseDto)
			.collect(Collectors.toList());
	}

	/*
	 * 予約済みの予約一覧を取得する。
	 * 
	 * @return 予約レスポンスDTOのリスト
	 */
	public List<ReservationResponseDTO> findAllConfirmed() {
		return reservationRepository.findByStatus(ReservationStatus.CONFIRMED)
			.stream().map(this::toResponseDto)
			.collect(Collectors.toList());
	}

	/*
	 * 予約IDを指定して予約情報を取得する。
	 * 
	 * <p>指定された予約IDの予約が存在しない場合は例外を発生させる。</p>
	 * 
	 * @param reservationId 予約ID
	 * @return 予約レスポンスDTO
	 * @throws ReservationNotFoundException 予約が存在しない場合
	 */
	public ReservationResponseDTO findById(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ReservationNotFoundException(reservationId));

		return toResponseDto(reservation);
	}
}
