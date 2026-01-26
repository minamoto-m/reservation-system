package jp.github.minamoto.m.reservationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.github.minamoto.m.reservationsystem.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
