CREATE INDEX idx_archive_teacher_status ON archive_record (teacher_id, status, archive_type);
CREATE INDEX idx_archive_created_at ON archive_record (created_at);
CREATE INDEX idx_log_module_created_at ON system_log (module, created_at);
CREATE INDEX idx_log_user_created_at ON system_log (username, created_at);
CREATE INDEX idx_result_template_rank ON evaluation_result (template_id, ranking_no);
CREATE INDEX idx_user_role_status ON sys_user (role, status);
