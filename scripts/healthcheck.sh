#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${1:-http://localhost:8080/api}"
echo "[healthcheck] checking ${BASE_URL}/health"
curl -fsS "${BASE_URL}/health" | sed 's/.*/[healthcheck] &/'
