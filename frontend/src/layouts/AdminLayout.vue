<template>
  <div class="layout-shell">
    <aside class="side-nav">
      <div class="brand-block">
        <div class="brand-title">教师档案管理考评系统</div>
        <div class="brand-subtitle">学校业务试运行版</div>
      </div>
      <el-menu :default-active="activeMenu" router class="nav-menu">
        <el-menu-item index="/dashboard">工作台</el-menu-item>
        <el-menu-item index="/archives">档案管理</el-menu-item>
        <el-menu-item index="/data">数据中心</el-menu-item>
        <el-menu-item index="/evaluation">考评管理</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/users">用户管理</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/logs">操作审计</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/login-audits">登录日志</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/ops">运维中心</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/backups">备份中心</el-menu-item>
      </el-menu>
    </aside>
    <div class="main-shell">
      <header class="top-bar">
        <div>
          <div class="page-title">{{ title }}</div>
          <div class="page-meta">{{ roleLabel }} · {{ realName || username }}</div>
        </div>
        <div class="toolbar-actions">
          <el-tag type="success">{{ environment }}</el-tag>
          <el-button link @click="goDashboard">返回工作台</el-button>
          <el-button type="danger" plain @click="logout">退出登录</el-button>
        </div>
      </header>
      <main class="content-shell">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const title = computed(() => route.meta?.title || '控制台')
const activeMenu = computed(() => route.path)
const username = computed(() => authStore.username || localStorage.getItem('username') || '-')
const realName = computed(() => authStore.realName || localStorage.getItem('realName') || '')
const isAdmin = computed(() => (authStore.role || localStorage.getItem('role')) === 'ADMIN')
const roleLabel = computed(() => isAdmin.value ? '管理员' : '教师')
const environment = import.meta.env.VITE_APP_ENV || 'dev'

const logout = () => {
  authStore.clearSession()
  router.replace('/login')
}
const goDashboard = () => router.push('/dashboard')
</script>
