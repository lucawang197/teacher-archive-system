package com.school.teacherarchivesystem.service;

import com.school.teacherarchivesystem.entity.EvaluationResult;
import com.school.teacherarchivesystem.repository.SystemUserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {
    private final EvaluationService evaluationService;
    private final ArchiveService archiveService;
    private final SystemUserRepository systemUserRepository;

    public ExportService(EvaluationService evaluationService, ArchiveService archiveService, SystemUserRepository systemUserRepository) {
        this.evaluationService = evaluationService;
        this.archiveService = archiveService;
        this.systemUserRepository = systemUserRepository;
    }

    public byte[] exportArchives() {
        List<Map<String, Object>> rows = archiveService.statsPerTeacher();
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("教科研数据");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("工号");
            header.createCell(1).setCellValue("姓名");
            header.createCell(2).setCellValue("学科");
            header.createCell(3).setCellValue("公开课");
            header.createCell(4).setCellValue("论文");
            header.createCell(5).setCellValue("比赛获奖");
            header.createCell(6).setCellValue("教研项目");
            header.createCell(7).setCellValue("成果合计");
            for (int i = 0; i < rows.size(); i++) {
                Map<String, Object> r = rows.get(i);
                long oc = toLong(r.get("openClassCount"));
                long pa = toLong(r.get("paperCount"));
                long co = toLong(r.get("competitionCount"));
                long rp = toLong(r.get("researchProjectCount"));
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(String.valueOf(r.getOrDefault("teacherNo", "")));
                row.createCell(1).setCellValue(String.valueOf(r.getOrDefault("teacherName", "")));
                row.createCell(2).setCellValue(String.valueOf(r.getOrDefault("subjectName", "")));
                row.createCell(3).setCellValue(oc);
                row.createCell(4).setCellValue(pa);
                row.createCell(5).setCellValue(co);
                row.createCell(6).setCellValue(rp);
                row.createCell(7).setCellValue(oc + pa + co + rp);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("导出失败：" + ex.getMessage(), ex);
        }
    }

    private long toLong(Object v) {
        return v instanceof Number n ? n.longValue() : 0L;
    }

    public byte[] exportResults(Long templateId) {
        List<EvaluationResult> rows = evaluationService.listResults(templateId);
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("考评结果");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("教师工号");
            header.createCell(1).setCellValue("姓名");
            header.createCell(2).setCellValue("学科");
            header.createCell(3).setCellValue("综合得分");
            header.createCell(4).setCellValue("排名");
            header.createCell(5).setCellValue("评分时间");
            header.createCell(6).setCellValue("备注");
            for (int i = 0; i < rows.size(); i++) {
                EvaluationResult item = rows.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getTeacher().getTeacherNo() == null ? "" : item.getTeacher().getTeacherNo());
                row.createCell(1).setCellValue(item.getTeacher().getRealName());
                row.createCell(2).setCellValue(item.getTeacher().getSubjectName() == null ? "" : item.getTeacher().getSubjectName());
                row.createCell(3).setCellValue(item.getTotalScore());
                row.createCell(4).setCellValue(item.getRankingNo() == null ? 0 : item.getRankingNo());
                row.createCell(5).setCellValue(item.getScoreTime() == null ? "" : item.getScoreTime().toString());
                row.createCell(6).setCellValue(item.getRemark() == null ? "" : item.getRemark());
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("导出失败：" + ex.getMessage(), ex);
        }
    }
}
