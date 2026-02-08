package jp.github.minamoto.m.reservationsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.github.minamoto.m.reservationsystem.dto.DepartmentResponseDTO;
import jp.github.minamoto.m.reservationsystem.service.DepartmentService;

@RestController
@RequestMapping("/v1/departments")
public class DepartmentController {
	private final DepartmentService departmentService;
	
	public DepartmentController(DepartmentService departmentService) { 
		this.departmentService = departmentService;
	}
	
	/*
	 * 診療科目一覧を取得する。
	 * 
	 * @return 診療科目レスポンス
	 */
	@GetMapping
	public List<DepartmentResponseDTO> findAll() {
		return departmentService.findAll();
	}
}
