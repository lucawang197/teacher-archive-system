# Changelog

## v9 - 工程硬化版

- 教师列表支持关键字筛选和分页。
- 教师批量导入接入真实 Excel 解析，新增模板下载。
- 关键实体增加 Jackson 忽略配置，避免密码哈希、文件真实路径等敏感信息直接暴露。
- 登录日志与操作审计后端接口拆分。
- 新增 Swagger/OpenAPI 支持。
- 新增 Flyway 索引脚本，提升日志、档案、评分结果和用户检索性能。
- 新增后端 / 前端 Dockerfile、源码构建版 docker-compose、GitHub Actions CI。
- 修复集成测试中的 JSON 字符串问题。
