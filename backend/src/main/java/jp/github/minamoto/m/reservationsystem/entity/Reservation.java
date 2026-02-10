package jp.github.minamoto.m.reservationsystem.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

	@OneToOne
	@JoinColumn(name = "time_slot_id", unique = true, nullable = false)
	private TimeSlot timeSlot;

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	private String name;
	private String phoneNumber;

	@CreationTimestamp
	private LocalDateTime createdAt;
}
