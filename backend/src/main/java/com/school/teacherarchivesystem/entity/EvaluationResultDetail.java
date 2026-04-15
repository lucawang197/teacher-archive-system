package com.school.teacherarchivesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "evaluation_result_detail")
public class EvaluationResultDetail extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false)
    private EvaluationResult result;

    @Column(name = "indicator_code", nullable = false, length = 50)
    private String indicatorCode;

    @Column(name = "indicator_name", nullable = false, length = 100)
    private String indicatorName;

    @Column(name = "raw_score", nullable = false)
    private Double rawScore;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "weighted_score", nullable = false)
    private Double weightedScore;

    public EvaluationResult getResult() { return result; }
    public void setResult(EvaluationResult result) { this.result = result; }
    public String getIndicatorCode() { return indicatorCode; }
    public void setIndicatorCode(String indicatorCode) { this.indicatorCode = indicatorCode; }
    public String getIndicatorName() { return indicatorName; }
    public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }
    public Double getRawScore() { return rawScore; }
    public void setRawScore(Double rawScore) { this.rawScore = rawScore; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getWeightedScore() { return weightedScore; }
    public void setWeightedScore(Double weightedScore) { this.weightedScore = weightedScore; }
}
