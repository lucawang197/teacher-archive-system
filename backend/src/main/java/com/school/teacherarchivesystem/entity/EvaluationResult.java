package com.school.teacherarchivesystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluation_result")
public class EvaluationResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationTemplate template;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private SystemUser teacher;

    @Column(name = "total_score", nullable = false)
    private Double totalScore;

    @Column(name = "ranking_no")
    private Integer rankingNo;

    @Column(name = "score_time", nullable = false)
    private LocalDateTime scoreTime;

    @Column(length = 200)
    private String remark;

    @Column(name = "missing_items_json", columnDefinition = "TEXT")
    private String missingItemsJson;

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationResultDetail> details = new ArrayList<>();

    public EvaluationTemplate getTemplate() { return template; }
    public void setTemplate(EvaluationTemplate template) { this.template = template; }
    public SystemUser getTeacher() { return teacher; }
    public void setTeacher(SystemUser teacher) { this.teacher = teacher; }
    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }
    public Integer getRankingNo() { return rankingNo; }
    public void setRankingNo(Integer rankingNo) { this.rankingNo = rankingNo; }
    public LocalDateTime getScoreTime() { return scoreTime; }
    public void setScoreTime(LocalDateTime scoreTime) { this.scoreTime = scoreTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getMissingItemsJson() { return missingItemsJson; }
    public void setMissingItemsJson(String missingItemsJson) { this.missingItemsJson = missingItemsJson; }
    public List<EvaluationResultDetail> getDetails() { return details; }
    public void setDetails(List<EvaluationResultDetail> details) { this.details = details; }
}
