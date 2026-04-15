<template>
  <div class="page-stack">
    <el-card>
      <template #header>
        <div class="section-header">
          <span>操作审计</span>
          <div class="toolbar-actions">
            <el-button type="primary" @click="handleExport">导出 CSV</el-button>
            <el-button @click="loadRows">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="filters" class="filter-form">
        <el-form-item label="操作人"><el-input v-model="filters.username" clearable /></el-form-item>
        <el-form-item label="模块"><el-input v-model="filters.module" clearable placeholder="如 ARCHIVE / EVALUATION" /></el-form-item>
        <el-form-item label="结果">
          <el-select v-model="filters.success" clearable style="width: 120px">
            <el-option :value="true" label="成功" />
            <el-option :value="false" label="失败" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadRows">查询</el-button></el-form-item>
      </el-form>

      <el-table :data="rows" empty-text="暂无日志记录">
        <el-table-column prop="createdAt" label="时间" width="180" />
        <el-table-column prop="username" label="操作人" width="120" />
        <el-table-column prop="module" label="模块" width="140" />
        <el-table-column prop="actionType" label="操作类型" width="160" />
        <el-table-column label="结果" width="100">
          <template #default="scope"><el-tag :type="scope.row.success ? 'success' : 'danger'">{{ scope.row.success ? '成功' : '失败' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP" width="150" />
        <el-table-column prop="detail" label="说明" min-width="260" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { downloadFile } from '../api/http'
import http from '../api/http'

const rows = ref([])
const filters = reactive({ username: '', module: '', success: undefined })

const loadRows = async () => {
  const res = await http.get('/logs', { params: { ...filters, page: 0, size: 100 } })
  rows.value = res.data.content || []
}

const handleExport = async () => {
  await downloadFile('/logs/export', 'system_logs.csv')
}

onMounted(loadRows)
</script>
