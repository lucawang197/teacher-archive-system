package com.school.teacherarchivesystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluation_indicator")
public class EvaluationIndicator extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationTemplate template;

    @Column(name = "indicator_code", nullable = false, length = 50)
    private String indicatorCode;

    @Column(name = "indicator_name", nullable = false, length = 100)
    private String indicatorName;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "score_rule_json", nullable = false, columnDefinition = "TEXT")
    private String scoreRuleJson;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @Column(nullable = false)
    private Boolean enabled = true;

    public EvaluationTemplate getTemplate() { return template; }
    public void setTemplate(EvaluationTemplate template) { this.template = template; }
    public String getIndicatorCode() { return indicatorCode; }
    public void setIndicatorCode(String indicatorCode) { this.indicatorCode = indicatorCode; }
    public String getIndicatorName() { return indicatorName; }
    public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getScoreRuleJson() { return scoreRuleJson; }
    public void setScoreRuleJson(String scoreRuleJson) { this.scoreRuleJson = scoreRuleJson; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
