CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    teacher_no VARCHAR(50) UNIQUE,
    subject_name VARCHAR(50),
    job_title VARCHAR(50),
    phone VARCHAR(30),
    email VARCHAR(100),
    failed_login_count INT NOT NULL DEFAULT 0,
    locked_until DATETIME NULL,
    last_login_ip VARCHAR(100),
    last_login_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE archive_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_id BIGINT NOT NULL,
    archive_name VARCHAR(100) NOT NULL,
    archive_type VARCHAR(30) NOT NULL,
    level VARCHAR(30),
    related_date DATE NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    review_comment VARCHAR(300),
    review_by BIGINT NULL,
    reviewed_at DATETIME NULL,
    deleted BIT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_archive_teacher FOREIGN KEY (teacher_id) REFERENCES sys_user(id),
    CONSTRAINT fk_archive_reviewer FOREIGN KEY (review_by) REFERENCES sys_user(id)
);
CREATE TABLE archive_attachment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    archive_id BIGINT NOT NULL,
    original_file_name VARCHAR(200) NOT NULL,
    stored_file_name VARCHAR(200) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attachment_archive FOREIGN KEY (archive_id) REFERENCES archive_record(id)
);
CREATE TABLE evaluation_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(100) NOT NULL,
    school_year VARCHAR(20),
    subject_name VARCHAR(50),
    active BIT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE evaluation_indicator (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    indicator_code VARCHAR(50) NOT NULL,
    indicator_name VARCHAR(100) NOT NULL,
    weight DOUBLE NOT NULL,
    score_rule_json TEXT NOT NULL,
    sort_order INT NOT NULL DEFAULT 1,
    enabled BIT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_indicator_template FOREIGN KEY (template_id) REFERENCES evaluation_template(id)
);
CREATE TABLE evaluation_result (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    total_score DOUBLE NOT NULL,
    ranking_no INT,
    score_time DATETIME NOT NULL,
    remark VARCHAR(200),
    missing_items_json TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_template FOREIGN KEY (template_id) REFERENCES evaluation_template(id),
    CONSTRAINT fk_result_teacher FOREIGN KEY (teacher_id) REFERENCES sys_user(id)
);
CREATE TABLE evaluation_result_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    result_id BIGINT NOT NULL,
    indicator_code VARCHAR(50) NOT NULL,
    indicator_name VARCHAR(100) NOT NULL,
    raw_score DOUBLE NOT NULL,
    weight DOUBLE NOT NULL,
    weighted_score DOUBLE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_detail_result FOREIGN KEY (result_id) REFERENCES evaluation_result(id)
);
CREATE TABLE system_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    username VARCHAR(50),
    action_type VARCHAR(50) NOT NULL,
    module VARCHAR(50) NOT NULL,
    biz_id BIGINT NULL,
    success BIT NOT NULL,
    ip_address VARCHAR(100),
    detail TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE backup_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    backup_name VARCHAR(100) NOT NULL,
    backup_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    remark VARCHAR(300),
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_backup_user FOREIGN KEY (created_by) REFERENCES sys_user(id)
);
