#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
source "${ROOT_DIR}/deploy/.env.example" 2>/dev/null || true
: "${MYSQL_HOST:=127.0.0.1}"
: "${MYSQL_PORT:=3306}"
: "${MYSQL_DATABASE:=teacher_archive_system}"
: "${MYSQL_USERNAME:=root}"
: "${MYSQL_PASSWORD:=123456}"
: "${BACKUP_DIR:=${ROOT_DIR}/backup_output}"
mkdir -p "${BACKUP_DIR}"
STAMP="$(date +%Y%m%d_%H%M%S)"
FILE="${BACKUP_DIR}/teacher_archive_system_${STAMP}.sql"
echo "[backup] exporting ${MYSQL_DATABASE} -> ${FILE}"
mysqldump -h"${MYSQL_HOST}" -P"${MYSQL_PORT}" -u"${MYSQL_USERNAME}" -p"${MYSQL_PASSWORD}" --single-transaction --routines --triggers "${MYSQL_DATABASE}" > "${FILE}"
echo "[backup] done"
