# 测试方案与自动化说明

后端已编写 `backend/src/test/java/com/school/teacherarchivesystem/AuthAndScoringIntegrationTest.java`，重点覆盖：
1. 登录成功，获取 JWT，访问工作台摘要
2. 教师上传档案
3. 管理员审核档案
4. 管理员执行自动评分
5. 管理员导出考评结果 Excel

当前沙箱没有 Maven，无法执行 `mvn test`。所以这次交付的是：
- 完整测试代码：已写
- 测试方案：已写
- 测试执行说明：已写
