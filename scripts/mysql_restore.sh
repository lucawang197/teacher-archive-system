#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
source "${ROOT_DIR}/deploy/.env.example" 2>/dev/null || true
: "${MYSQL_HOST:=127.0.0.1}"
: "${MYSQL_PORT:=3306}"
: "${MYSQL_DATABASE:=teacher_archive_system}"
: "${MYSQL_USERNAME:=root}"
: "${MYSQL_PASSWORD:=123456}"
FILE="${1:-}"
if [[ -z "${FILE}" || ! -f "${FILE}" ]]; then
  echo "usage: $0 /path/to/backup.sql" >&2
  exit 1
fi
echo "[restore] importing ${FILE} -> ${MYSQL_DATABASE}"
mysql -h"${MYSQL_HOST}" -P"${MYSQL_PORT}" -u"${MYSQL_USERNAME}" -p"${MYSQL_PASSWORD}" "${MYSQL_DATABASE}" < "${FILE}"
echo "[restore] done"
