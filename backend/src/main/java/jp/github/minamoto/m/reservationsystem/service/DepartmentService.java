package jp.github.minamoto.m.reservationsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jp.github.minamoto.m.reservationsystem.dto.DepartmentResponseDTO;
import jp.github.minamoto.m.reservationsystem.entity.Department;
import jp.github.minamoto.m.reservationsystem.repository.DepartmentRepository;

@Service
public class DepartmentService {
	private final DepartmentRepository departmentRepository;
	
	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}
	
	/*
	 * Departmentエンティティをレスポンス用DTOに変換する。
	 * 
	 * @param department 診療科目を表すDepartmentエンティティ
	 * @return DepartmentResponseDTO
	 */
	private DepartmentResponseDTO toDTO(Department entity) {
		DepartmentResponseDTO dto = new DepartmentResponseDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		
		return dto;
	}
	
	/*
	 * 診療科目一覧を取得する。
	 * 
	 * @return 診療科目一覧レスポンス
	 */
	public List<DepartmentResponseDTO> findAll() {
		List<Department> departments = departmentRepository.findAll();

		return departments.stream().map(this::toDTO).collect(Collectors.toList());
	}
}
