package jp.github.minamoto.m.reservationsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.github.minamoto.m.reservationsystem.dto.DoctorResponseDTO;
import jp.github.minamoto.m.reservationsystem.service.DoctorService;

@RestController
@RequestMapping("/v1/doctors")
public class DoctorController {
	private final DoctorService doctorService;
	
	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}
	
	/*
	 * 医師一覧を取得する。
	 * 
	 * @return 医師一覧レスポンス
	 */
	@GetMapping
	public List<DoctorResponseDTO> findAll() {
		return doctorService.findAll();
	}

	/*
	 * 診療科目IDを指定して医師情報を取得する。
	 * 
	 * @param departmentId 診療科目ID
	 * @return 医師情報レスポンス
	 */
	@GetMapping("/{departmentId}")
	public List<DoctorResponseDTO> findByDepartmentId(@PathVariable Long departmentId) {
		return doctorService.findByDepartmentId(departmentId);
	}
}
