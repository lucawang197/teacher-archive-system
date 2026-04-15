#!/usr/bin/env bash
set -euo pipefail

echo '[1/5] 检查 Java 版本'
java -version

echo '[2/5] 检查 Node 版本'
node -v

echo '[3/5] 检查 MySQL 客户端'
mysql --version

echo '[4/5] 检查 Nginx'
nginx -v

echo '[5/5] 检查必要目录'
mkdir -p ./data/uploads ./data/backups ./logs
ls -ld ./data/uploads ./data/backups ./logs

echo '部署前基础检查完成'
