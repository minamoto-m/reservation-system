package jp.github.minamoto.m.reservationsystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jp.github.minamoto.m.reservationsystem.domain.ReservationStatus;
import lombok.Data;

@Data
public class ReservationCreateRequestDto {
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate reservationDate;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;
	
	private ReservationStatus status;
	private String name;
	private String phoneNumber;
}