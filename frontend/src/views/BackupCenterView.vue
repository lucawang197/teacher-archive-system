<template>
  <div class="page-stack">
    <el-card>
      <template #header>
        <div class="section-header">
          <span>备份中心</span>
          <div class="toolbar-actions">
            <el-button type="primary" @click="openCreateDialog">发起手动备份</el-button>
            <el-button @click="loadRows">刷新</el-button>
          </div>
        </div>
      </template>

      <el-alert type="warning" show-icon :closable="false" title="正式环境建议通过 mysqldump + 附件目录打包统一备份。本页面用于业务层触发和留痕。" />

      <el-table :data="rows" style="margin-top: 16px">
        <el-table-column prop="backupName" label="备份名称" min-width="180" />
        <el-table-column prop="backupType" label="类型" width="120" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="remark" label="备注" min-width="220" />
        <el-table-column prop="filePath" label="文件路径" min-width="240" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="handleRestore(scope.row)">触发恢复</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="createVisible" title="发起手动备份" width="540px">
      <el-form :model="editor" label-width="110px">
        <el-form-item label="备份名称">
          <el-input v-model="editor.backupName" placeholder="例如：上线前备份_20260414" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editor.remark" type="textarea" :rows="4" placeholder="填写备份原因和业务背景" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCreate">确认发起</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../api/http'

const rows = ref([])
const createVisible = ref(false)
const editor = reactive({ backupName: '', remark: '' })

const loadRows = async () => {
  const res = await http.get('/backups', { params: { page: 0, size: 50 } })
  rows.value = res.data.content || []
}

const openCreateDialog = () => {
  editor.backupName = ''
  editor.remark = ''
  createVisible.value = true
}

const submitCreate = async () => {
  if (!editor.backupName) {
    ElMessage.warning('请填写备份名称')
    return
  }
  await http.post('/backups', editor)
  ElMessage.success('手动备份请求已提交')
  createVisible.value = false
  loadRows()
}

const handleRestore = async (row) => {
  await ElMessageBox.confirm(`确认基于备份“${row.backupName}”发起恢复请求吗？`, '恢复确认', { type: 'warning' })
  await http.post(`/backups/${row.id}/restore`)
  ElMessage.success('恢复请求已记录，请按运维流程执行')
}

onMounted(loadRows)
</script>
