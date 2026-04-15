<template>
  <div class="page-stack">
    <el-card>
      <template #header>
        <div class="section-header">
          <span>{{ isAdmin ? '教科研数据中心' : '我的教科研数据' }}</span>
          <div class="toolbar-actions">
            <el-button v-if="isAdmin" type="primary" @click="handleExport">导出 Excel</el-button>
            <el-button @click="loadRows">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="filters" class="filter-form">
        <el-form-item label="教师姓名" v-if="isAdmin">
          <el-input v-model="filters.teacherName" placeholder="前端演示筛选" clearable />
        </el-form-item>
        <el-form-item label="学科">
          <el-input v-model="filters.subjectName" placeholder="按学科筛选" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="applyFilter">筛选</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="displayRows" empty-text="暂无数据">
        <el-table-column prop="teacherNo" label="工号" width="140" />
        <el-table-column prop="teacherName" label="姓名" width="120" />
        <el-table-column prop="subjectName" label="学科" width="120" />
        <el-table-column prop="openClassCount" label="公开课" width="100" sortable />
        <el-table-column prop="paperCount" label="论文" width="100" sortable />
        <el-table-column prop="competitionCount" label="比赛获奖" width="120" sortable />
        <el-table-column prop="researchProjectCount" label="教研项目" width="120" sortable />
        <el-table-column label="成果合计" width="120">
          <template #default="scope">
            {{ totalCount(scope.row) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="24" :md="12">
        <el-card>
          <template #header>统计摘要</template>
          <div class="summary-grid">
            <div class="summary-box">
              <span>公开课总数</span>
              <strong>{{ totals.openClassCount }}</strong>
            </div>
            <div class="summary-box">
              <span>论文总数</span>
              <strong>{{ totals.paperCount }}</strong>
            </div>
            <div class="summary-box">
              <span>比赛获奖总数</span>
              <strong>{{ totals.competitionCount }}</strong>
            </div>
            <div class="summary-box">
              <span>教研项目总数</span>
              <strong>{{ totals.researchProjectCount }}</strong>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card>
          <template #header>说明</template>
          <el-alert :closable="false" type="info" show-icon title="当前页面展示的是后端汇总后的统计结果。正式版本可继续补充图表组件，将按学科、按年度的统计可视化展示出来。" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import http, { downloadFile } from '../api/http'

const role = localStorage.getItem('role')
const isAdmin = computed(() => role === 'ADMIN')
const sourceRows = ref([])
const displayRows = ref([])
const filters = reactive({ teacherName: '', subjectName: '' })

const totals = computed(() => displayRows.value.reduce((acc, item) => {
  acc.openClassCount += Number(item.openClassCount || 0)
  acc.paperCount += Number(item.paperCount || 0)
  acc.competitionCount += Number(item.competitionCount || 0)
  acc.researchProjectCount += Number(item.researchProjectCount || 0)
  return acc
}, { openClassCount: 0, paperCount: 0, competitionCount: 0, researchProjectCount: 0 }))

const totalCount = (row) => Number(row.openClassCount || 0) + Number(row.paperCount || 0) + Number(row.competitionCount || 0) + Number(row.researchProjectCount || 0)

const loadRows = async () => {
  if (isAdmin.value) {
    const res = await http.get('/archives/stats')
    sourceRows.value = res.data || []
  } else {
    const res = await http.get('/archives/mine', { params: { page: 0, size: 100 } })
    const list = res.data.content || []
    const summary = {
      teacherNo: localStorage.getItem('username') || '-',
      teacherName: localStorage.getItem('realName') || '-',
      subjectName: '-',
      openClassCount: list.filter((item) => item.archiveType === 'OPEN_CLASS').length,
      paperCount: list.filter((item) => item.archiveType === 'PAPER').length,
      competitionCount: list.filter((item) => item.archiveType === 'COMPETITION').length,
      researchProjectCount: list.filter((item) => item.archiveType === 'RESEARCH_PROJECT').length,
    }
    sourceRows.value = [summary]
  }
  applyFilter()
}

const applyFilter = () => {
  displayRows.value = sourceRows.value.filter((item) => {
    const teacherMatch = !filters.teacherName || String(item.teacherName || '').includes(filters.teacherName)
    const subjectMatch = !filters.subjectName || String(item.subjectName || '').includes(filters.subjectName)
    return teacherMatch && subjectMatch
  })
}

const resetFilter = () => {
  filters.teacherName = ''
  filters.subjectName = ''
  applyFilter()
}

const handleExport = async () => {
  await downloadFile('/exports/archives', `教师教科研数据_${new Date().toISOString().slice(0, 10)}.xlsx`)
}

onMounted(loadRows)
</script>

<style scoped>
.page-stack { display: flex; flex-direction: column; gap: 16px; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.toolbar-actions { display: flex; gap: 12px; }
.filter-form { margin-bottom: 16px; }
.summary-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
.summary-box { background: #f5f7fa; border-radius: 14px; padding: 16px; display: flex; flex-direction: column; gap: 8px; }
.summary-box span { color: #909399; }
.summary-box strong { font-size: 26px; }
</style>
