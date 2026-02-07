package jp.github.minamoto.m.reservationsystem.dto;

import lombok.Data;

@Data
public class DoctorResponseDTO {
	private Long id;
	private String name;
	private Long departmentId;
}
