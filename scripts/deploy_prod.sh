#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "${ROOT_DIR}/deploy"
echo "[deploy] starting docker compose for production-like environment"
docker compose up -d --build
echo "[deploy] containers started"
echo "[deploy] tip: run ../scripts/healthcheck.sh http://localhost/api after services are ready"
