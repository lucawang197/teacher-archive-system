package com.school.teacherarchivesystem.repository;

import com.school.teacherarchivesystem.entity.EvaluationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationResultRepository extends JpaRepository<EvaluationResult, Long> {
    List<EvaluationResult> findByTemplateIdOrderByRankingNoAsc(Long templateId);
    List<EvaluationResult> findByTemplateIdAndTeacherIdOrderByRankingNoAsc(Long templateId, Long teacherId);
    void deleteByTemplateId(Long templateId);
}
