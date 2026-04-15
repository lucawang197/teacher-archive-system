#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080/api}"
ADMIN_USER="${ADMIN_USER:-admin}"
ADMIN_PASS="${ADMIN_PASS:-123456}"

if ! command -v curl >/dev/null 2>&1; then
  echo "curl 未安装" >&2
  exit 1
fi
if ! command -v python3 >/dev/null 2>&1; then
  echo "python3 未安装" >&2
  exit 1
fi

extract_json_field() {
  python3 - "$1" <<'PY'
import json,sys
text=sys.stdin.read()
obj=json.loads(text)
path=sys.argv[1].split('.')
cur=obj
for p in path:
    cur=cur[p]
print(cur)
PY
}

echo "[1/5] 管理员登录"
LOGIN_RES=$(curl -sS -X POST "$BASE_URL/auth/login" \
  -H 'Content-Type: application/json' \
  -d "{\"username\":\"$ADMIN_USER\",\"password\":\"$ADMIN_PASS\"}")
TOKEN=$(printf '%s' "$LOGIN_RES" | extract_json_field data.token)

echo "[2/5] 获取工作台摘要"
curl -sS "$BASE_URL/dashboard/summary" -H "Authorization: Bearer $TOKEN" >/dev/null

echo "[3/5] 获取教师列表"
curl -sS "$BASE_URL/users/teachers?page=0&size=10" -H "Authorization: Bearer $TOKEN" >/dev/null

echo "[4/5] 获取档案汇总"
curl -sS "$BASE_URL/archives/summary" -H "Authorization: Bearer $TOKEN" >/dev/null

echo "[5/5] 获取评分模板列表"
curl -sS "$BASE_URL/evaluations/templates" -H "Authorization: Bearer $TOKEN" >/dev/null

echo "Smoke test completed successfully."
