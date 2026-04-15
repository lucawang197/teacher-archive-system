<template>
  <div class="page-stack">
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card>
          <div class="metric-title">服务状态</div>
          <div class="metric-value" :class="health.status === 'UP' ? 'ok' : 'bad'">{{ health.status || '未知' }}</div>
          <div class="metric-tip">{{ health.service || '-' }} / {{ health.version || '-' }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="metric-title">运行环境</div>
          <div class="metric-value">{{ health.profile || '-' }}</div>
          <div class="metric-tip">接口联调与生产切换前请核对环境</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="metric-title">最近检查时间</div>
          <div class="metric-value small">{{ health.timestamp || '-' }}</div>
          <div class="metric-tip">可用于部署验收和巡检记录</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="section-header">
          <span>备份记录</span>
          <div class="toolbar-actions">
            <el-button @click="loadHealth">刷新健康状态</el-button>
            <el-button @click="loadBackups">刷新备份记录</el-button>
          </div>
        </div>
      </template>
      <el-table :data="backups" empty-text="暂无备份记录">
        <el-table-column prop="backupName" label="备份名称" min-width="240" />
        <el-table-column prop="backupType" label="类型" width="120" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="filePath" label="路径" min-width="280" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
      </el-table>
    </el-card>

    <el-card>
      <template #header><span>上线前巡检建议</span></template>
      <el-timeline>
        <el-timeline-item timestamp="1">确认学校提供的应用服务器、数据库与附件存储路径。</el-timeline-item>
        <el-timeline-item timestamp="2">修改 JWT 密钥、数据库密码、默认账号密码和 Nginx 域名。</el-timeline-item>
        <el-timeline-item timestamp="3">执行数据库迁移并导入初始管理员。</el-timeline-item>
        <el-timeline-item timestamp="4">执行 API 冒烟测试、登录测试、上传审核评分主流程。</el-timeline-item>
        <el-timeline-item timestamp="5">完成学校管理员培训并交付运维手册、备份恢复脚本和验收清单。</el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import http from '../api/http'

const health = ref({})
const backups = ref([])

const loadHealth = async () => {
  const res = await http.get('/health')
  health.value = res.data || {}
}

const loadBackups = async () => {
  const res = await http.get('/backups', { params: { page: 0, size: 20 } })
  backups.value = res.data.content || []
}

onMounted(() => {
  loadHealth()
  loadBackups()
})
</script>

<style scoped>
.page-stack { display: flex; flex-direction: column; gap: 16px; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.toolbar-actions { display: flex; gap: 12px; }
.metric-title { color: #909399; font-size: 13px; margin-bottom: 12px; }
.metric-value { font-size: 28px; font-weight: 700; }
.metric-value.small { font-size: 18px; }
.metric-value.ok { color: #67c23a; }
.metric-value.bad { color: #f56c6c; }
.metric-tip { margin-top: 8px; color: #909399; font-size: 12px; }
</style>
