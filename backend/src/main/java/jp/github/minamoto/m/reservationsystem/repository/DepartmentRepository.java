package jp.github.minamoto.m.reservationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.github.minamoto.m.reservationsystem.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
