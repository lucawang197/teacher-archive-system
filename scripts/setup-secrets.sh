#!/usr/bin/env bash
# 用法: bash scripts/setup-secrets.sh
# 依赖: gh cli 已登录 (gh auth login)

set -euo pipefail

REPO="lucawang197/teacher-archive-system"

# ── 修改这里 ──────────────────────────────────────────
DEPLOY_HOST="47.116.173.186"
DEPLOY_USER="root"
SSH_KEY_PATH="$HOME/.ssh/deploy_teacher"   # 部署专用私钥路径
GHCR_TOKEN=""                              # GitHub Token (read:packages)
DB_USERNAME="tas_user"
DB_PASSWORD="MyPass2026!"
MYSQL_ROOT_PASSWORD="RootPass2026!"
# ─────────────────────────────────────────────────────

if [[ -z "$GHCR_TOKEN" ]]; then
  echo "ERROR: 请先填写 GHCR_TOKEN"
  exit 1
fi

JWT_SECRET=$(openssl rand -hex 32)

echo "→ 设置 Secrets 到 $REPO ..."

gh secret set DEPLOY_HOST          --body "$DEPLOY_HOST"          -R "$REPO"
gh secret set DEPLOY_USER          --body "$DEPLOY_USER"          -R "$REPO"
gh secret set DEPLOY_SSH_KEY       < "$SSH_KEY_PATH"              -R "$REPO"
gh secret set GHCR_TOKEN           --body "$GHCR_TOKEN"           -R "$REPO"
gh secret set DB_USERNAME          --body "$DB_USERNAME"          -R "$REPO"
gh secret set DB_PASSWORD          --body "$DB_PASSWORD"          -R "$REPO"
gh secret set MYSQL_ROOT_PASSWORD  --body "$MYSQL_ROOT_PASSWORD"  -R "$REPO"
gh secret set JWT_SECRET           --body "$JWT_SECRET"           -R "$REPO"

echo "✓ 全部设置完成"
echo "  JWT_SECRET=$JWT_SECRET  (已写入 GitHub，本地备份请自行保存)"
