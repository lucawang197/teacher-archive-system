<template>
  <div class="page-stack">
    <el-row :gutter="16">
      <el-col :xs="24" :md="12" :xl="6" v-for="item in metrics" :key="item.label">
        <el-card class="metric-card">
          <div class="metric-title">{{ item.label }}</div>
          <div class="metric-value">{{ item.value }}</div>
          <div class="metric-tip">{{ item.tip }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card>
          <template #header>
            <div class="section-header"><span>本周工作建议</span></div>
          </template>
          <el-timeline>
            <el-timeline-item timestamp="1" placement="top">教师上传公开课、论文、竞赛、教研项目材料</el-timeline-item>
            <el-timeline-item timestamp="2" placement="top">管理员按学科完成材料审核与退回说明</el-timeline-item>
            <el-timeline-item timestamp="3" placement="top">教科研数据汇总、导出和异常数据修正</el-timeline-item>
            <el-timeline-item timestamp="4" placement="top">应用评分模板执行自动评分并形成结果</el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card>
          <template #header>
            <div class="section-header"><span>快捷入口</span></div>
          </template>
          <div class="quick-grid">
            <el-button @click="go('/archives')">档案管理</el-button>
            <el-button @click="go('/data')">数据中心</el-button>
            <el-button @click="go('/evaluation')">考评管理</el-button>
            <el-button v-if="isAdmin" @click="go('/users')">用户管理</el-button>
            <el-button v-if="isAdmin" @click="go('/logs')">操作审计</el-button>
            <el-button v-if="isAdmin" @click="go('/backups')">备份中心</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isAdmin = computed(() => localStorage.getItem('role') === 'ADMIN')
const metrics = computed(() => {
  if (isAdmin.value) {
    return [
      { label: '待审核档案', value: '12', tip: '优先处理最近 3 天新提交材料' },
      { label: '本月评分任务', value: '3', tip: '请核对模板是否已生效' },
      { label: '异常数据', value: '2', tip: '导出前请先修正汇总错误' },
      { label: '最近备份', value: '今日 02:00', tip: '建议重大操作前手动备份' },
    ]
  }
  return [
    { label: '我的档案数', value: '8', tip: '建议补齐近一年教科研材料' },
    { label: '待审核', value: '2', tip: '管理员审核后会显示结果' },
    { label: '本学年得分', value: '86.5', tip: '按当前有效模板自动汇总' },
    { label: '最近更新', value: '今天', tip: '请及时完善个人信息' },
  ]
})

const go = (path) => router.push(path)
</script>
