package com.school.teacherarchivesystem.dto.user;

import java.util.ArrayList;
import java.util.List;

public class UserImportResult {
    private int totalRows;
    private int successCount;
    private int failedCount;
    private final List<String> errors = new ArrayList<>();

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    public List<String> getErrors() { return errors; }
}
