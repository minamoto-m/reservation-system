INSERT INTO department(name) VALUES
('内科'),
('外科'),
('皮膚科'),
('小児科');

INSERT INTO doctor(name, department_id) VALUES
('テストドクター1', 4),
('テストドクター2', 2),
('テストドクター3', 1),
('テストドクター4', 4),
('テストドクター5', 3),
('テストドクター6', 3),
('テストドクター7', 1),
('テストドクター8', 1),
('テストドクター9', 1),
('テストドクター10', 2);

INSERT INTO time_slot(date, start_time, end_time, doctor_id, status) VALUES
(CURRENT_DATE, '09:00', '09:30', 1, 'OPEN'),
(CURRENT_DATE, '09:30', '10:00', 1, 'OPEN'),
(CURRENT_DATE, '10:00', '10:30', 1, 'OPEN'),
(CURRENT_DATE, '10:30', '11:00', 1, 'OPEN'),
(CURRENT_DATE, '09:00', '09:30', 2, 'OPEN'),
(CURRENT_DATE, '09:30', '10:00', 2, 'OPEN');