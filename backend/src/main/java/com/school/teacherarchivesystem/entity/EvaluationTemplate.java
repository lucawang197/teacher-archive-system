package com.school.teacherarchivesystem.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluation_template")
public class EvaluationTemplate extends BaseEntity {
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;
    @Column(name = "school_year", length = 20)
    private String schoolYear;
    @Column(name = "subject_name", length = 50)
    private String subjectName;
    @Column(nullable = false)
    private Boolean active = false;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sortOrder asc")
    private List<EvaluationIndicator> indicators = new ArrayList<>();

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public List<EvaluationIndicator> getIndicators() { return indicators; }
    public void setIndicators(List<EvaluationIndicator> indicators) { this.indicators = indicators; }
}
