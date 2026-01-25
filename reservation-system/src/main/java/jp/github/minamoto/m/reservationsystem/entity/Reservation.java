package jp.github.minamoto.m.reservationsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import lombok.Data;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate reservationDate;
	private LocalTime startTime;
	private LocalTime endTime;
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	private String name;
	private String phoneNumber;
	private LocalDateTime createdAt;
}
