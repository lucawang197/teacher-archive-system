# Swagger 与接口调试说明

后端引入了 springdoc-openapi，启动后可直接访问：

- Swagger UI：`/swagger-ui/index.html`
- OpenAPI JSON：`/v3/api-docs`

建议在学校内网联调时：

1. 先通过管理员账号登录获取 JWT。
2. 在 Swagger 右上角 Authorize 中填入 `Bearer <token>`。
3. 优先验证登录、用户管理、档案管理、评分、导出、备份与日志接口。
4. 现场演示时建议配合 `docs/postman/TeacherArchiveSystem.postman_collection.json` 做回归。
