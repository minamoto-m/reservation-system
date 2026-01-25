package jp.github.minamoto.m.reservationsystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ReservationResponseDto {
		
	private Long id;
	private LocalDate reservationDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String status;
	private String name;
}

