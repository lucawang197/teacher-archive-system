<template>
  <div class="login-shell">
    <div class="login-panel">
      <div class="login-heading">
        <h1>教师档案管理考评系统</h1>
        <p>适用于学校教师档案采集、审核、统计与自动考评的综合管理平台</p>
      </div>
      <el-form :model="form" label-position="top" @keyup.enter="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberMe">记住当前账号</el-checkbox>
        </el-form-item>
        <el-button type="primary" :loading="loading" class="login-button" @click="submit">登录系统</el-button>
      </el-form>
      <div class="login-tips">
        <div>演示账号：</div>
        <div>管理员：admin / 123456</div>
        <div>教师：teacher01 / 123456</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const rememberMe = ref(true)
const form = reactive({ username: '', password: '' })

onMounted(() => {
  const remembered = localStorage.getItem('rememberedUsername')
  if (remembered) form.username = remembered
})

const submit = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await authStore.login(form)
    if (rememberMe.value) {
      localStorage.setItem('rememberedUsername', form.username)
    } else {
      localStorage.removeItem('rememberedUsername')
    }
    ElMessage.success('登录成功')
    router.replace('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>
