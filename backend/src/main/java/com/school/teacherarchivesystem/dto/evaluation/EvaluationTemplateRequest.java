package com.school.teacherarchivesystem.dto.evaluation;

import java.util.List;

public class EvaluationTemplateRequest {
    private String templateName;
    private String schoolYear;
    private String subjectName;
    private List<EvaluationIndicatorRequest> indicators;

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public List<EvaluationIndicatorRequest> getIndicators() { return indicators; }
    public void setIndicators(List<EvaluationIndicatorRequest> indicators) { this.indicators = indicators; }
}
