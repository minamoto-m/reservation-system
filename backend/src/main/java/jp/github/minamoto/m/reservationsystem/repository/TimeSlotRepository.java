package jp.github.minamoto.m.reservationsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import jp.github.minamoto.m.reservationsystem.domain.TimeSlotStatus;
import jp.github.minamoto.m.reservationsystem.entity.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<TimeSlot> findById(Long id);
	List<TimeSlot> findByDoctorIdAndDateAndStatusOrderByStartTimeAsc(Long doctorId, LocalDate date, TimeSlotStatus status);
}
