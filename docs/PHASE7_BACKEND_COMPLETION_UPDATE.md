# 第七阶段更新说明

本轮重点不是继续补文档，而是补齐 Java 后端缺失的基础代码，目标是把工程从“交付骨架”推进到“更接近可编译联调”的状态。

本轮新增与完善：

1. 补齐公共层：统一响应、业务异常、全局异常处理。
2. 补齐配置层：AppProperties、默认 application.yml、application-test.yml。
3. 补齐实体层：用户、档案、附件、考评模板、考评指标、评分结果、评分明细、系统日志、备份记录。
4. 补齐 Repository 层：用户、档案、附件、模板、指标、结果、日志、备份。
5. 补齐安全层：JWT Token Provider、JWT 过滤器、登录用户对象、权限工具。
6. 补齐业务层：登录、首页汇总、档案管理、考评执行、结果导出、用户管理。
7. 补齐控制层：Auth、Dashboard、Archive、Evaluation、Export、User。
8. 修复自动化测试中的 JSON 字符串错误，补上测试 profile 配置。

说明：
- 当前环境仍然没有 Maven 依赖下载能力，所以这一轮仍无法在沙箱里真实执行 `mvn test` 或 `mvn package`。
- 但后端源码已经不再只是零散 controller，而是有了完整的分层骨架和核心业务实现，后续可以在有 Maven 的机器上继续编译联调。
