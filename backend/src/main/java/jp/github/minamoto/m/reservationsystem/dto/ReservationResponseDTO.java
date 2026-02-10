package jp.github.minamoto.m.reservationsystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ReservationResponseDTO {
	private Long reservationId;
	private Long timeSlotId;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private String status;
	private String name;
}
