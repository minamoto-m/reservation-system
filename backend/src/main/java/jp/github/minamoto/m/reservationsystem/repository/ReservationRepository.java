package jp.github.minamoto.m.reservationsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByStatus(ReservationStatus status);
}
