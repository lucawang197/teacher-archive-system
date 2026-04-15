<template>
  <div class="page-stack">
    <el-card>
      <template #header>
        <div class="section-header">
          <span>登录日志</span>
          <div class="toolbar-actions"><el-button @click="loadRows">刷新</el-button></div>
        </div>
      </template>
      <el-alert type="info" :closable="false" show-icon title="当前版本通过日志模块筛选 AUTH 登录相关记录展示登录日志。" />
      <el-table :data="rows" style="margin-top: 16px">
        <el-table-column prop="createdAt" label="时间" width="180" />
        <el-table-column prop="username" label="账号" width="140" />
        <el-table-column prop="ipAddress" label="IP 地址" width="150" />
        <el-table-column prop="actionType" label="动作" width="160" />
        <el-table-column label="结果" width="100">
          <template #default="scope"><el-tag :type="scope.row.success ? 'success' : 'danger'">{{ scope.row.success ? '成功' : '失败' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="detail" label="说明" min-width="260" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import http from '../api/http'

const rows = ref([])
const loadRows = async () => {
  const res = await http.get('/logs', { params: { module: 'AUTH', page: 0, size: 100 } })
  rows.value = res.data.content || []
}
onMounted(loadRows)
</script>
