# 需求追踪矩阵（节选）

| 需求条目 | 实现位置 | 当前状态 | 说明 |
|---|---|---|---|
| 登录、超时、角色跳转 | backend/controller/AuthController.java, frontend/views/LoginView.vue | 已实现 | JWT 鉴权，管理员/教师角色区分 |
| 个人信息管理 | backend/controller/UserController.java | 已实现 | 支持查询、编辑、改密 |
| 教师档案上传 | backend/controller/ArchiveController.java, frontend/views/ArchiveCenterView.vue | 已实现 | 支持 PDF/JPG/PNG、多附件 |
| 管理员审核 | ArchiveController.review, ArchiveCenterView.vue | 已实现 | 支持通过/驳回与审核意见 |
| 附件预览/下载 | AttachmentController.java | 已实现 | 新增在线预览与下载接口 |
| 数据展示、筛选、导出 | DataCenterView.vue, ExportController.java | 已实现 | 支持汇总与 Excel 导出 |
| 考评模板与自动评分 | EvaluationController.java, EvaluationCenterView.vue | 已实现 | 支持模板生效、执行评分、备注 |
| 用户管理与批量导入 | UserController.java | 已实现 | 含启用/禁用 |
| 数据备份/恢复 | BackupController.java, scripts/mysql_backup.sh, scripts/mysql_restore.sh | 已补强 | 后台记录 + 数据库脚本 |
| 系统日志、审计导出 | LogController.java, LogView.vue | 已补强 | 支持筛选与 CSV 导出 |
| 部署与运维 | deploy/docker-compose.yml, docs/OPS_RUNBOOK.md | 已补强 | 支持 Linux + Nginx + Docker Compose |
