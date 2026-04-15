INSERT INTO sys_user (username, password_hash, role, status, real_name, teacher_no, subject_name, job_title, phone, email, created_at, updated_at)
VALUES
('admin', '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'ADMIN', 'ENABLED', '系统管理员', 'A0001', '信息中心', '管理员', '13800000000', 'admin@school.local', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('teacher01', '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED', '张老师', 'T0001', '语文', '一级教师', '13800000001', 'teacher01@school.local', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('teacher02', '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED', '李老师', 'T0002', '数学', '高级教师', '13800000002', 'teacher02@school.local', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO evaluation_template (template_name, school_year, subject_name, active, created_at, updated_at)
VALUES ('默认教师考评模板', '2025-2026', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO evaluation_indicator (template_id, indicator_code, indicator_name, weight, score_rule_json, sort_order, enabled, created_at, updated_at)
VALUES
(1, 'OPEN_CLASS', '公开课', 25.0, '{"sourceField":"openClassCount","unitScore":10,"maxScore":100}', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'PAPER', '论文', 35.0, '{"sourceField":"paperCount","unitScore":15,"maxScore":100}', 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'COMPETITION', '比赛获奖', 20.0, '{"sourceField":"competitionCount","unitScore":20,"maxScore":100}', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'RESEARCH_PROJECT', '教研项目', 20.0, '{"sourceField":"researchProjectCount","unitScore":25,"maxScore":100}', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
