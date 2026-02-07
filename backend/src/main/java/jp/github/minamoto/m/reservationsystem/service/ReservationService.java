package jp.github.minamoto.m.reservationsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDto;
import jp.github.minamoto.m.reservationsystem.dto.ReservationResponseDto;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;
import jp.github.minamoto.m.reservationsystem.repository.ReservationRepository;
import jp.github.minamoto.m.reservationsystem.service.exception.ReservationNotFoundException;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	/*
	 * Reservationエンティティをレスポンス用DTOに変換する。
	 * 
	 * @param entity 予約情報を表すReservationエンティティ
	 * 
	 * @return 予約情報を表すReservationResponseDto
	 */
	private ReservationResponseDto toDto(Reservation entity) {
		ReservationResponseDto dto = new ReservationResponseDto();
		dto.setReservationDate(entity.getReservationDate());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		dto.setStatus(entity.getStatus().name());
		dto.setName(entity.getName());

		return dto;
	}

	/*
	 * 予約を作成する。
	 * 
	 * @param 予約作成リクエストDTO
	 * @return 作成された予約情報
	 */
	public Reservation create(ReservationCreateRequestDto dto) {
		Reservation r = new Reservation();
		r.setReservationDate(dto.getReservationDate());
		r.setStartTime(dto.getStartTime());
		r.setEndTime(dto.getEndTime());
		r.setStatus(dto.getStatus());
		r.setName(dto.getName());
		r.setPhoneNumber(dto.getPhoneNumber());
		r.setCreatedAt(LocalDateTime.now());

		return reservationRepository.save(r);
	}

	/*
	 * 予約一覧を取得する。
	 * 
	 * @return 予約一覧レスポンス
	 */
	public List<ReservationResponseDto> findAll() {
		List<Reservation> reservations = reservationRepository.findAll();

		return reservations.stream().map(this::toDto).collect(Collectors.toList());
	}

	/*
	 * 有効な予約一覧を取得する。
	 * 
	 * @return 予約一覧レスポンス
	 */
	public List<ReservationResponseDto> findAllActive() {
		return reservationRepository.findByStatus(ReservationStatus.ACTIVE).stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	/*
	 * 予約IDを指定して予約情報を取得する。
	 * 
	 * <p>指定されたIDの予約が存在しない場合は例外を発生させる。</p>
	 * 
	 * @param id 予約ID
	 * @return 予約情報のレスポンスDTO
	 * @throws ReservationNotFoundException 予約が存在しない場合
	 */
	public ReservationResponseDto findById(Long id) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));

		return toDto(reservation);
	}

	/*
	 * 予約IDを指定して予約をキャンセルする。
	 * 
	 * <p>DBから予約データは削除せず、statusをCANCELLEDに更新する。</p>
	 * 
	 * @param id 予約ID
	 */
	public void cancel(Long id) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));

		reservation.setStatus(ReservationStatus.CANCELLED);

		reservationRepository.save(reservation);
	}
}
