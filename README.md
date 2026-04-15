# 教师档案管理考评系统（正式工程版源码）

这是按 Java + Spring Boot + MySQL + Vue + Nginx + Linux 技术路线整理的一版正式工程源码，覆盖教师档案管理、管理员审核、数据汇总、Excel 导出、考评模板、自动评分、用户管理、日志审计、备份恢复等核心能力。

## 当前交付说明

本交付已经把正式项目源码、SQL、自动化测试代码、部署文件、接口文档都搭好了。

但需要说明一个现实约束：当前沙箱环境里没有 Maven / Gradle，也没有可用的 Spring 依赖缓存，因此我无法在这里把 Spring Boot 项目依赖真正下载下来后编译运行。

你拿到源码后，在本地或服务器安装 Maven 后即可执行完整构建。

## 推荐本地运行步骤

### 后端
```bash
cd backend
mvn clean test
mvn spring-boot:run
```

### 前端
```bash
cd frontend
npm install
npm run dev
```

### 默认账号
- 管理员：admin / 123456
- 教师：teacher01 / 123456
- 教师：teacher02 / 123456
