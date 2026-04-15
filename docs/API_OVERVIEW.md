# 接口总览

## 鉴权
- POST /api/auth/login
- POST /api/auth/forgot-password

## 用户
- POST /api/users
- PUT /api/users/{id}
- GET /api/users/teachers
- POST /api/users/change-password
- POST /api/users/import

## 档案
- POST /api/archives
- PUT /api/archives/{id}
- DELETE /api/archives/{id}
- GET /api/archives/mine
- GET /api/archives
- GET /api/archives/{id}
- POST /api/archives/{id}/review
- GET /api/archives/summary

## 考评
- POST /api/evaluations/templates
- PUT /api/evaluations/templates/{id}/activate
- GET /api/evaluations/templates
- POST /api/evaluations/run
- GET /api/evaluations/results
- PUT /api/evaluations/results/{id}/remark
