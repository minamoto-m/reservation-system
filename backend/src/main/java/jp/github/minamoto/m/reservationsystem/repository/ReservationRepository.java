package jp.github.minamoto.m.reservationsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByStatus(ReservationStatus status);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Reservation> findById(Long id);
}
