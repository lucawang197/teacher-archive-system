package com.school.teacherarchivesystem.dto.evaluation;

public class EvaluationIndicatorRequest {
    private String indicatorCode;
    private String indicatorName;
    private Double weight;
    private String scoreRuleJson;
    private Integer sortOrder;
    private Boolean enabled;

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
