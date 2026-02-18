-- MySQL 用 DDL（Docker / 本番）
CREATE TABLE app_user (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(50) NOT NULL
);

CREATE TABLE department (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL
);

CREATE TABLE doctor (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	department_id BIGINT NOT NULL,
	CONSTRAINT fk_doctor_department
		FOREIGN KEY (department_id)
		REFERENCES department(id)
);

CREATE TABLE time_slot (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	date DATE NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	doctor_id BIGINT NOT NULL,
	status VARCHAR(20) NOT NULL,
	CONSTRAINT fk_time_slot_doctor
		FOREIGN KEY (doctor_id)
		REFERENCES doctor(id)
);

CREATE TABLE reservation (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	time_slot_id BIGINT NOT NULL,
	status VARCHAR(20) NOT NULL,
	name VARCHAR(255),
	phone_number VARCHAR(255),
	created_at TIMESTAMP NULL,
	CONSTRAINT fk_reservation_time_slot
		FOREIGN KEY (time_slot_id)
		REFERENCES time_slot(id)
);
