<template>
  <div class="page-stack">
    <el-card>
      <template #header>
        <div class="section-header">
          <span>用户管理</span>
          <div class="toolbar-actions">
            <el-button type="primary" @click="openCreateDialog">新增教师</el-button>
            <el-button @click="loadRows">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="rows" empty-text="暂无教师数据">
        <el-table-column prop="teacherNo" label="工号" width="140" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="subjectName" label="学科" width="120" />
        <el-table-column prop="jobTitle" label="职称" width="140" />
        <el-table-column prop="phone" label="联系方式" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ENABLED' ? 'success' : 'info'">
              {{ scope.row.status === 'ENABLED' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button text type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button text @click="toggleStatus(scope.row)">{{ scope.row.status === 'ENABLED' ? '禁用' : '启用' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card>
      <template #header>批量导入说明</template>
      <el-alert
        type="info"
        :closable="false"
        show-icon
        title="当前后端已经支持通过 Excel 批量导入教师用户。正式演示时可按 username、realName、teacherNo、subjectName 四列准备模板，再调用 /users/import 接口。"
      />
    </el-card>

    <el-dialog v-model="editorVisible" :title="editingId ? '编辑教师' : '新增教师'" width="640px">
      <el-form :model="editor" label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="editor.username" :disabled="!!editingId" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editor.realName" />
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="editor.teacherNo" :disabled="!!editingId" />
        </el-form-item>
        <el-form-item label="学科">
          <el-input v-model="editor.subjectName" />
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="editor.jobTitle" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="editor.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editor.email" />
        </el-form-item>
        <el-form-item label="初始密码" v-if="!editingId">
          <el-input v-model="editor.initialPassword" placeholder="默认 123456" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editorVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditor">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../api/http'

const rows = ref([])
const editorVisible = ref(false)
const editingId = ref(null)
const editor = reactive({ username: '', realName: '', teacherNo: '', subjectName: '', jobTitle: '', phone: '', email: '', initialPassword: '123456' })

const loadRows = async () => {
  const res = await http.get('/users/teachers', { params: { page: 0, size: 100 } })
  rows.value = res.data.content || []
}

const resetEditor = () => {
  editingId.value = null
  editor.username = ''
  editor.realName = ''
  editor.teacherNo = ''
  editor.subjectName = ''
  editor.jobTitle = ''
  editor.phone = ''
  editor.email = ''
  editor.initialPassword = '123456'
}

const openCreateDialog = () => {
  resetEditor()
  editorVisible.value = true
}

const openEditDialog = (row) => {
  editingId.value = row.id
  editor.username = row.username
  editor.realName = row.realName
  editor.teacherNo = row.teacherNo
  editor.subjectName = row.subjectName
  editor.jobTitle = row.jobTitle
  editor.phone = row.phone
  editor.email = row.email
  editorVisible.value = true
}

const submitEditor = async () => {
  if (editingId.value) {
    await http.put(`/users/${editingId.value}`, {
      realName: editor.realName,
      subjectName: editor.subjectName,
      jobTitle: editor.jobTitle,
      phone: editor.phone,
      email: editor.email,
    })
    ElMessage.success('用户更新成功')
  } else {
    await http.post('/users', {
      username: editor.username,
      realName: editor.realName,
      teacherNo: editor.teacherNo,
      subjectName: editor.subjectName,
      jobTitle: editor.jobTitle,
      phone: editor.phone,
      email: editor.email,
      role: 'TEACHER',
      initialPassword: editor.initialPassword,
    })
    ElMessage.success('用户创建成功')
  }
  editorVisible.value = false
  await loadRows()
}

const toggleStatus = async (row) => {
  const targetStatus = row.status === 'ENABLED' ? 'DISABLED' : 'ENABLED'
  await http.put(`/users/${row.id}`, { status: targetStatus })
  ElMessage.success(targetStatus === 'ENABLED' ? '用户已启用' : '用户已禁用')
  await loadRows()
}

onMounted(loadRows)
</script>

<style scoped>
.page-stack { display: flex; flex-direction: column; gap: 16px; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.toolbar-actions { display: flex; gap: 12px; }
</style>
