package jp.github.minamoto.m.reservationsystem.dto;

import lombok.Data;

@Data
public class ReservationCreateRequestDTO {
	private Long timeSlotId;
	private String name;
	private String phoneNumber;
}