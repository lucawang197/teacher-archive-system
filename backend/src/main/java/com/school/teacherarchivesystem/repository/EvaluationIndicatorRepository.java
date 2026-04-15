package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.EvaluationIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationIndicatorRepository extends JpaRepository<EvaluationIndicator, Long> {
    List<EvaluationIndicator> findByTemplateIdAndEnabledTrueOrderBySortOrderAsc(Long templateId);
}
