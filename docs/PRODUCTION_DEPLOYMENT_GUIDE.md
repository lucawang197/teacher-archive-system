# 生产部署指南

## 1. 推荐部署架构
- Linux 服务器（建议 Ubuntu 22.04 / CentOS Stream 9）
- Nginx 作为反向代理与静态资源服务
- Spring Boot 作为后端服务
- MySQL 8.0 作为数据库
- 附件目录与数据库备份目录建议单独挂载数据盘

## 2. 服务器准备
- JDK 21
- Maven 3.9+
- Node.js 20+
- MySQL 8.0+
- Nginx 1.20+

## 3. 环境变量与配置
- 后端参考 `application-prod.yml`
- 前端参考 `.env.production`
- 如需 Docker 部署，可参考 `deploy/docker-compose.yml`

## 4. 首次部署步骤
1. 初始化数据库并执行 Flyway。
2. 配置附件目录、备份目录。
3. 构建后端 jar。
4. 构建前端 dist。
5. 配置 Nginx 反向代理。
6. 执行健康检查与冒烟验证。

## 5. 上线后核查
- 登录是否正常
- 附件上传与预览是否正常
- 导出是否正常
- 审计日志是否落库
- 定时备份是否正常
