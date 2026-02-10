package jp.github.minamoto.m.reservationsystem.dto;

import lombok.Data;

@Data
public class ReservationCancelResponseDTO {
	private Long reservationId;
	private String status;
}