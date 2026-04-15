-- ============================================================
-- 用户：1 管理员 + 8 教师（密码统一 password123）
-- ============================================================
INSERT INTO sys_user (username, password_hash, role, status, real_name, teacher_no, subject_name, job_title, phone, email, created_at, updated_at)
VALUES
('admin',      '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'ADMIN',   'ENABLED',  '系统管理员', 'A0001', '信息中心', '管理员',   '13800000000', 'admin@school.local',      CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('zhang_san',  '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '张三',      'T0001', '语文',   '一级教师', '13800000001', 'zhangsan@school.local',   CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('li_si',      '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '李四',      'T0002', '数学',   '高级教师', '13800000002', 'lisi@school.local',       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('wang_wu',    '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '王五',      'T0003', '英语',   '一级教师', '13800000003', 'wangwu@school.local',     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('zhao_liu',   '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '赵六',      'T0004', '物理',   '二级教师', '13800000004', 'zhaoliu@school.local',    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('chen_qi',    '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '陈七',      'T0005', '化学',   '高级教师', '13800000005', 'chenqi@school.local',     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sun_ba',     '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '孙八',      'T0006', '历史',   '一级教师', '13800000006', 'sunba@school.local',      CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('zhou_jiu',   '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'DISABLED', '周九',      'T0007', '地理',   '二级教师', '13800000007', 'zhoujiu@school.local',    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('wu_shi',     '$2b$10$sxV1x.fyriijZ.9hMzbNfeuAGC28NG7gpbywwPhWaYjn6ebCmCADW', 'TEACHER', 'ENABLED',  '吴十',      'T0008', '生物',   '高级教师', '13800000008', 'wushi@school.local',      CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 考评模板：2023-2024（归档）+ 2024-2025（启用）
-- ============================================================
INSERT INTO evaluation_template (template_name, school_year, subject_name, active, created_at, updated_at)
VALUES
('教师综合考评模板 2023-2024', '2023-2024', NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('教师综合考评模板 2024-2025', '2024-2025', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 模板1（2023-2024）指标
INSERT INTO evaluation_indicator (template_id, indicator_code, indicator_name, weight, score_rule_json, sort_order, enabled, created_at, updated_at)
VALUES
(1, 'OPEN_CLASS',        '公开课',   25.0, '{"sourceField":"openClassCount","unitScore":10,"maxScore":100}', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'PAPER',             '论文',     35.0, '{"sourceField":"paperCount","unitScore":15,"maxScore":100}',    2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'COMPETITION',       '比赛获奖', 20.0, '{"sourceField":"competitionCount","unitScore":20,"maxScore":100}', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'RESEARCH_PROJECT',  '教研项目', 20.0, '{"sourceField":"researchProjectCount","unitScore":25,"maxScore":100}', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 模板2（2024-2025）指标
INSERT INTO evaluation_indicator (template_id, indicator_code, indicator_name, weight, score_rule_json, sort_order, enabled, created_at, updated_at)
VALUES
(2, 'OPEN_CLASS',        '公开课',   25.0, '{"sourceField":"openClassCount","unitScore":10,"maxScore":100}', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'PAPER',             '论文',     35.0, '{"sourceField":"paperCount","unitScore":15,"maxScore":100}',    2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'COMPETITION',       '比赛获奖', 20.0, '{"sourceField":"competitionCount","unitScore":20,"maxScore":100}', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'RESEARCH_PROJECT',  '教研项目', 20.0, '{"sourceField":"researchProjectCount","unitScore":25,"maxScore":100}', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 档案记录（teacher_id 2~9 = zhang_san~wu_shi，review_by=1=admin）
-- ============================================================
-- 张三 - 语文
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(2, '2024年市级语文公开课', 'OPEN_CLASS', '市级', '2024-03-15', '在市级语文教研活动中讲授《荷塘月色》公开课，效果良好。', 'APPROVED', '内容充实，教学方法新颖，予以通过。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '论文《浅谈小学语文阅读教学》', 'PAPER', '省级', '2024-06-01', '发表于《语文教学通讯》2024年第3期。', 'APPROVED', '论文质量较高，逻辑清晰。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '全国语文大赛优秀奖', 'COMPETITION', '国家级', '2024-09-20', '参加全国中小学语文教师教学大赛获优秀奖。', 'PENDING', NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '校级语文教研项目', 'RESEARCH_PROJECT', '校级', '2024-01-10', '主持学校语文课程改革研究项目。', 'APPROVED', '项目成果显著，批准归档。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 李四 - 数学
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(3, '省级数学公开课', 'OPEN_CLASS', '省级', '2024-04-22', '在省教育厅组织的示范课活动中讲授《函数与方程》。', 'APPROVED', '逻辑严密，层次分明，通过。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '论文《高中数学函数教学探讨》', 'PAPER', '国家级', '2024-05-15', '发表于《数学通报》2024年第5期。', 'APPROVED', '理论与实践结合好，高质量论文。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '华罗庚金杯数学邀请赛辅导奖', 'COMPETITION', '国家级', '2024-10-08', '辅导学生参加华罗庚金杯邀请赛，获三等奖。', 'APPROVED', '获奖记录真实，批准归档。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '市级数学教研课题立项', 'RESEARCH_PROJECT', '市级', '2024-02-28', '申报并获批市教育局"初中数学建模教学实践"课题。', 'PENDING', NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 王五 - 英语
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(4, '校级英语口语公开课', 'OPEN_CLASS', '校级', '2024-03-05', '开展英语口语教学公开课，学生参与度高。', 'APPROVED', '教学设计合理，通过审核。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '论文《任务型英语教学法的实践探索》', 'PAPER', '市级', '2024-07-01', '发表于《中学英语教学》2024年第4期。', 'REJECTED', '论文数据来源需补充说明，退回修改。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '全市英语演讲比赛优秀指导教师', 'COMPETITION', '市级', '2024-11-12', '指导学生在全市英语演讲比赛中获二等奖，本人获优秀指导教师称号。', 'APPROVED', '材料齐全，予以通过。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '英语课程校本化开发研究', 'RESEARCH_PROJECT', '校级', '2024-09-01', '主持英语校本课程开发项目。', 'PENDING', NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 赵六 - 物理
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(5, '市级物理实验公开课', 'OPEN_CLASS', '市级', '2024-05-18', '展示物理实验课《牛顿第三定律验证》，实验环节设计出色。', 'APPROVED', '实验操作规范，教学效果好。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '论文《初中物理实验探究式教学》', 'PAPER', '省级', '2024-08-20', '发表于《物理教学》2024年第6期。', 'PENDING', NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '省物理竞赛优秀辅导教师', 'COMPETITION', '省级', '2024-10-25', '辅导学生获省物理竞赛二等奖，获优秀辅导教师。', 'APPROVED', '成绩突出，批准归档。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 陈七 - 化学
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(6, '省级化学实验公开课', 'OPEN_CLASS', '省级', '2024-04-10', '在全省化学实验教学研讨会上展示《氧化还原反应》实验课。', 'APPROVED', '实验设计安全规范，教学内容深入浅出。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, '论文《绿色化学理念在中学教学中的应用》', 'PAPER', '国家级', '2024-06-15', '发表于《化学教育》2024年第7期。', 'APPROVED', '选题新颖，具有较高学术价值。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, '全国化学竞赛优秀辅导教师', 'COMPETITION', '国家级', '2024-11-05', '辅导学生获全国化学竞赛三等奖，获优秀辅导教师称号。', 'APPROVED', '成绩优异，予以通过。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, '省级化学教研项目负责人', 'RESEARCH_PROJECT', '省级', '2024-03-01', '主持省教育厅"中学化学实验创新"教研项目。', 'APPROVED', '项目进展顺利，成果丰富。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, '校级化学实验室建设方案', 'RESEARCH_PROJECT', '校级', '2024-07-15', '主导完成实验室升级改造方案设计。', 'PENDING', NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 孙八 - 历史
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(7, '校级历史公开课', 'OPEN_CLASS', '校级', '2024-03-20', '开展《近代中国史》公开课，史料丰富，学生反响热烈。', 'APPROVED', '教学内容饱满，通过。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, '论文《史料实证在历史课堂的运用》', 'PAPER', '市级', '2024-05-30', '发表于《历史教学》2024年第4期。', 'APPROVED', '论文观点明确，有实践意义。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, '市历史知识竞赛辅导奖', 'COMPETITION', '市级', '2024-12-01', '指导学生参加市历史知识竞赛，获团体二等奖。', 'PENDING', NULL, NULL, NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 吴十 - 生物
INSERT INTO archive_record (teacher_id, archive_name, archive_type, level, related_date, description, status, review_comment, review_by, reviewed_at, deleted, created_at, updated_at) VALUES
(9, '省级生物公开课', 'OPEN_CLASS', '省级', '2024-04-28', '在省级生物教研活动中开展《细胞的生命活动》公开课。', 'APPROVED', '教学思路清晰，实验演示规范。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, '论文《项目式学习在高中生物中的应用》', 'PAPER', '省级', '2024-06-20', '发表于《生物学教学》2024年第5期。', 'APPROVED', '研究成果具有推广价值。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, '全国生物竞赛辅导奖', 'COMPETITION', '国家级', '2024-10-15', '辅导学生获全国生物联赛二等奖。', 'APPROVED', '成绩优秀，批准归档。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, '省级生命科学教研课题', 'RESEARCH_PROJECT', '省级', '2024-02-10', '主持省"高中生物跨学科教学实践"教研项目。', 'APPROVED', '项目成果丰富，质量高。', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 考评结果（基于模板1 / 2023-2024，覆盖4位教师）
-- ============================================================
INSERT INTO evaluation_result (template_id, teacher_id, total_score, ranking_no, score_time, remark, created_at, updated_at)
VALUES
(1, 2, 87.5, 2, CURRENT_TIMESTAMP, '综合表现优秀', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 3, 92.0, 1, CURRENT_TIMESTAMP, '成绩突出，排名第一', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 6, 85.0, 3, CURRENT_TIMESTAMP, '档案完整，表现良好', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 9, 88.0, 2, CURRENT_TIMESTAMP, '论文和竞赛成绩均优', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 考评明细（result_id 1 = 张三）
INSERT INTO evaluation_result_detail (result_id, indicator_code, indicator_name, raw_score, weight, weighted_score, created_at, updated_at)
VALUES
(1, 'OPEN_CLASS',       '公开课',   80.0, 25.0, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'PAPER',            '论文',     85.0, 35.0, 29.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'COMPETITION',      '比赛获奖',  0.0, 20.0,  0.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'RESEARCH_PROJECT', '教研项目', 100.0, 20.0, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 考评明细（result_id 2 = 李四）
INSERT INTO evaluation_result_detail (result_id, indicator_code, indicator_name, raw_score, weight, weighted_score, created_at, updated_at)
VALUES
(2, 'OPEN_CLASS',       '公开课',   90.0, 25.0, 22.5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'PAPER',            '论文',    100.0, 35.0, 35.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'COMPETITION',      '比赛获奖', 100.0, 20.0, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'RESEARCH_PROJECT', '教研项目',  0.0, 20.0,  0.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 考评明细（result_id 3 = 陈七）
INSERT INTO evaluation_result_detail (result_id, indicator_code, indicator_name, raw_score, weight, weighted_score, created_at, updated_at)
VALUES
(3, 'OPEN_CLASS',       '公开课',   100.0, 25.0, 25.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'PAPER',            '论文',     100.0, 35.0, 35.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'COMPETITION',      '比赛获奖', 100.0, 20.0, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'RESEARCH_PROJECT', '教研项目',  25.0, 20.0,  5.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 考评明细（result_id 4 = 吴十）
INSERT INTO evaluation_result_detail (result_id, indicator_code, indicator_name, raw_score, weight, weighted_score, created_at, updated_at)
VALUES
(4, 'OPEN_CLASS',       '公开课',   90.0, 25.0, 22.5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'PAPER',            '论文',     85.0, 35.0, 29.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'COMPETITION',      '比赛获奖', 100.0, 20.0, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'RESEARCH_PROJECT', '教研项目', 100.0, 20.0, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
