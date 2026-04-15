# GitHub 持续集成说明

本项目已经补充 GitHub Actions 持续集成配置，默认支持以下能力：

## 1. 基础 CI
工作流文件：`.github/workflows/ci.yml`

触发时机：
- push 到 `main` / `master` / `develop`
- pull request 到 `main` / `master` / `develop`
- 手动触发 `workflow_dispatch`

执行内容：
1. 后端执行 `mvn test`
2. 后端执行 `mvn -DskipTests package`
3. 上传后端 jar 构建产物
4. 前端执行 `npm ci` / `npm install`
5. 前端执行 `npm run build`
6. 上传前端 `dist` 构建产物
7. 执行后端和前端 Docker 构建检查

## 2. Docker 镜像发布
工作流文件：`.github/workflows/release-docker.yml`

触发时机：
- 推送 `v*.*.*` 格式标签
- 手动触发

执行内容：
- 登录 GitHub Container Registry (GHCR)
- 构建后端镜像并推送到 GHCR
- 构建前端镜像并推送到 GHCR

## 3. 建议在 GitHub 仓库中配置的 Secrets
如后续接入正式发布或部署，建议在仓库 Settings → Secrets and variables → Actions 中配置：

- `PROD_HOST`
- `PROD_PORT`
- `PROD_USERNAME`
- `PROD_SSH_KEY`
- `PROD_DEPLOY_PATH`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`

当前这版 CI 不强依赖以上 Secret，只有将来扩展自动部署时才需要。

## 4. 使用方式
### 方式一：提交代码自动触发
```bash
 git add .
 git commit -m "feat: update teacher archive system"
 git push origin main
```

### 方式二：发起 Pull Request
创建 PR 后，GitHub 会自动执行 CI。

### 方式三：手动触发
进入仓库 `Actions` 页面，选择对应工作流，点击 `Run workflow`。

## 5. 推荐仓库保护策略
建议对 `main` 分支开启：
- Require a pull request before merging
- Require status checks to pass before merging
- Require branches to be up to date before merging

建议至少要求以下检查通过后再合并：
- `Backend Test`
- `Frontend Build`
- `Docker Build Check`

## 6. 注意事项
1. 后端依赖 Maven，前端依赖 Node.js 20。
2. 前端缓存路径使用 `package-lock.json`，首次运行如果不存在 lock 文件，工作流会自动回退到 `npm install`。
3. 如果未来要接入测试环境自动部署，可以在当前基础上再新增 `deploy-staging.yml`。
