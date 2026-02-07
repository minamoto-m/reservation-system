package jp.github.minamoto.m.reservationsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jp.github.minamoto.m.reservationsystem.dto.DoctorResponseDTO;
import jp.github.minamoto.m.reservationsystem.entity.Doctor;
import jp.github.minamoto.m.reservationsystem.repository.DoctorRepository;

@Service
public class DoctorService {
	private final DoctorRepository doctorRepository;
	
	public DoctorService(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}
	
	/*
	 * Doctorエンティティをレスポンス用DTOに変換する。
	 * 
	 * @param doctor 医師を表すDoctorエンティティ
	 * @return DoctorResponseDTO
	 */
	private DoctorResponseDTO toDTO(Doctor entity) {
		DoctorResponseDTO dto = new DoctorResponseDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDepartmentId(null);
		
		return dto;
	}
	
	/*
	 * 医師一覧を取得する。
	 * 
	 * @return 医師一覧レスポンス
	 */
	public List<DoctorResponseDTO> findAll() {
		List<Doctor> departments = doctorRepository.findAll();
		
		return departments.stream().map(this::toDTO).collect(Collectors.toList());
	}
}
