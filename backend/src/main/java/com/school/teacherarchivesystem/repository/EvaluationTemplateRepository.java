package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.EvaluationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationTemplateRepository extends JpaRepository<EvaluationTemplate, Long> {
    Optional<EvaluationTemplate> findByActiveTrue();
}
