# Postman / Apifox 导入说明

## Postman 导入
1. 打开 Postman。
2. 选择 Import。
3. 导入 `docs/postman/TeacherArchiveSystem.postman_collection.json`。
4. 再导入 `docs/postman/TeacherArchiveSystem.postman_environment.json`。
5. 执行登录接口后，将 token 写入环境变量。

## Apifox 导入
1. 新建项目。
2. 选择导入 Postman Collection。
3. 导入同名 JSON 文件。
4. 在环境变量中配置 `baseUrl` 和 `token`。
