<template>
  <div class="page-stack">
    <el-card>
      <template #header>
        <div class="section-header">
          <span>档案管理</span>
          <div class="toolbar-actions">
            <el-button v-if="isTeacher" type="primary" @click="openCreateDialog">上传档案</el-button>
            <el-button @click="loadRows">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="档案名称">
          <el-input v-model="query.teacherName" clearable placeholder="管理员可按教师姓名筛选" :disabled="isTeacher" />
        </el-form-item>
        <el-form-item label="档案类型">
          <el-select v-model="query.archiveType" clearable placeholder="全部类型" style="width: 180px">
            <el-option v-for="item in archiveTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="isAdmin" label="审核状态">
          <el-select v-model="query.status" clearable placeholder="全部状态" style="width: 160px">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadRows">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="rows" empty-text="暂无档案数据">
        <el-table-column prop="archiveName" label="档案名称" min-width="200" />
        <el-table-column prop="archiveType" label="档案类型" width="140">
          <template #default="scope">{{ archiveTypeText(scope.row.archiveType) }}</template>
        </el-table-column>
        <el-table-column v-if="isAdmin" prop="teacher.realName" label="教师" width="120">
          <template #default="scope">{{ scope.row.teacher?.realName }}</template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="100" />
        <el-table-column prop="relatedDate" label="相关时间" width="140" />
        <el-table-column prop="status" label="审核状态" width="120">
          <template #default="scope">
            <el-tag :type="statusTagType(scope.row.status)">{{ statusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewComment" label="审核意见" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="scope">
            <el-button text type="primary" @click="showDetail(scope.row)">详情</el-button>
            <el-button
              v-if="isTeacher && scope.row.status !== 'APPROVED'"
              text
              type="primary"
              @click="openEditDialog(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="isTeacher && scope.row.status !== 'APPROVED'"
              text
              type="danger"
              @click="removeArchive(scope.row)"
            >删除</el-button>
            <el-button v-if="isAdmin && scope.row.status === 'PENDING'" text type="success" @click="openReviewDialog(scope.row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-wrap">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="page.pageNo + 1"
          :page-size="page.pageSize"
          :total="page.total"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="editorVisible" :title="editingId ? '编辑档案' : '上传档案'" width="680px">
      <el-form :model="editor" label-width="100px">
        <el-form-item label="档案名称">
          <el-input v-model="editor.archiveName" maxlength="100" />
        </el-form-item>
        <el-form-item label="档案类型">
          <el-select v-model="editor.archiveType" style="width: 100%">
            <el-option v-for="item in archiveTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="级别">
          <el-input v-model="editor.level" placeholder="如校级、市级、省级" />
        </el-form-item>
        <el-form-item label="相关时间">
          <el-date-picker v-model="editor.relatedDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="editor.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            ref="uploadRef"
            multiple
            :auto-upload="false"
            :limit="5"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="fileList"
          >
            <template #trigger>
              <el-button>选择附件</el-button>
            </template>
            <template #tip>
              <div class="upload-tip">支持 PDF/JPG/PNG，最多 5 个附件，单文件不超过 50MB。</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editorVisible = false">取消</el-button>
        <el-button type="primary" @click="submitArchive">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="reviewVisible" title="档案审核" width="520px">
      <el-descriptions :column="1" border size="small" v-if="currentRow">
        <el-descriptions-item label="档案名称">{{ currentRow.archiveName }}</el-descriptions-item>
        <el-descriptions-item label="教师">{{ currentRow.teacher?.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ archiveTypeText(currentRow.archiveType) }}</el-descriptions-item>
      </el-descriptions>
      <el-form :model="reviewForm" label-width="88px" style="margin-top: 16px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="reviewForm.approved">
            <el-radio :label="true">通过</el-radio>
            <el-radio :label="false">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="reviewForm.reviewComment" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="档案详情" width="680px">
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="档案名称">{{ currentRow.archiveName }}</el-descriptions-item>
        <el-descriptions-item label="档案类型">{{ archiveTypeText(currentRow.archiveType) }}</el-descriptions-item>
        <el-descriptions-item label="级别">{{ currentRow.level || '-' }}</el-descriptions-item>
        <el-descriptions-item label="相关时间">{{ currentRow.relatedDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">{{ statusText(currentRow.status) }}</el-descriptions-item>
        <el-descriptions-item label="审核意见">{{ currentRow.reviewComment || '-' }}</el-descriptions-item>
        <el-descriptions-item label="说明" :span="2">{{ currentRow.description || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-alert v-if="currentRow?.attachments?.length" show-icon type="info" :closable="false" title="当前版本已返回附件元数据，正式联调时可补充在线预览与下载地址。" style="margin-top: 16px" />
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../api/http'

const role = localStorage.getItem('role')
const isTeacher = computed(() => role === 'TEACHER')
const isAdmin = computed(() => role === 'ADMIN')
const rows = ref([])
const page = reactive({ pageNo: 0, pageSize: 10, total: 0 })
const query = reactive({ teacherName: '', archiveType: '', status: '' })
const editorVisible = ref(false)
const reviewVisible = ref(false)
const detailVisible = ref(false)
const currentRow = ref(null)
const editingId = ref(null)
const selectedFiles = ref([])
const fileList = ref([])
const editor = reactive({ archiveName: '', archiveType: 'OPEN_CLASS', level: '', relatedDate: '', description: '' })
const reviewForm = reactive({ approved: true, reviewComment: '' })

const archiveTypeOptions = [
  { label: '公开课', value: 'OPEN_CLASS' },
  { label: '论文', value: 'PAPER' },
  { label: '比赛证书', value: 'COMPETITION' },
  { label: '教研项目', value: 'RESEARCH_PROJECT' },
]
const statusOptions = [
  { label: '待审核', value: 'PENDING' },
  { label: '已通过', value: 'APPROVED' },
  { label: '已驳回', value: 'REJECTED' },
]

const archiveTypeText = (value) => archiveTypeOptions.find((item) => item.value === value)?.label || value
const statusText = (value) => statusOptions.find((item) => item.value === value)?.label || value
const statusTagType = (status) => ({ PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }[status] || 'info')

const loadRows = async () => {
  const params = { page: page.pageNo, size: page.pageSize }
  if (isAdmin.value) {
    if (query.teacherName) params.teacherName = query.teacherName
    if (query.archiveType) params.archiveType = query.archiveType
    if (query.status) params.status = query.status
    const res = await http.get('/archives', { params })
    rows.value = res.data.content || []
    page.total = res.data.totalElements || 0
  } else {
    const res = await http.get('/archives/mine', { params })
    rows.value = res.data.content || []
    page.total = res.data.totalElements || 0
  }
}

const handlePageChange = (value) => {
  page.pageNo = value - 1
  loadRows()
}

const resetQuery = () => {
  query.teacherName = ''
  query.archiveType = ''
  query.status = ''
  page.pageNo = 0
  loadRows()
}

const resetEditor = () => {
  editor.archiveName = ''
  editor.archiveType = 'OPEN_CLASS'
  editor.level = ''
  editor.relatedDate = ''
  editor.description = ''
  editingId.value = null
  selectedFiles.value = []
  fileList.value = []
}

const openCreateDialog = () => {
  resetEditor()
  editorVisible.value = true
}

const openEditDialog = (row) => {
  editingId.value = row.id
  editor.archiveName = row.archiveName
  editor.archiveType = row.archiveType
  editor.level = row.level
  editor.relatedDate = row.relatedDate
  editor.description = row.description
  selectedFiles.value = []
  fileList.value = []
  editorVisible.value = true
}

const handleFileChange = (_, files) => {
  fileList.value = files
  selectedFiles.value = files.map((item) => item.raw).filter(Boolean)
}

const handleFileRemove = (_, files) => {
  fileList.value = files
  selectedFiles.value = files.map((item) => item.raw).filter(Boolean)
}

const submitArchive = async () => {
  if (!editor.archiveName || !editor.archiveType || !editor.relatedDate) {
    ElMessage.warning('请补齐档案名称、类型和相关时间')
    return
  }
  if (editingId.value) {
    await http.put(`/archives/${editingId.value}`, { ...editor })
  } else {
    const formData = new FormData()
    formData.append('form', new Blob([JSON.stringify(editor)], { type: 'application/json' }))
    selectedFiles.value.forEach((file) => formData.append('files', file))
    await http.post('/archives', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
  }
  ElMessage.success(editingId.value ? '档案更新成功' : '档案提交成功')
  editorVisible.value = false
  await loadRows()
}

const showDetail = async (row) => {
  const res = await http.get(`/archives/${row.id}`)
  currentRow.value = res.data
  detailVisible.value = true
}

const removeArchive = async (row) => {
  await ElMessageBox.confirm(`确认删除档案“${row.archiveName}”吗？`, '删除确认', { type: 'warning' })
  await http.delete(`/archives/${row.id}`)
  ElMessage.success('删除成功')
  await loadRows()
}

const openReviewDialog = (row) => {
  currentRow.value = row
  reviewForm.approved = true
  reviewForm.reviewComment = ''
  reviewVisible.value = true
}

const submitReview = async () => {
  await http.post(`/archives/${currentRow.value.id}/review`, { ...reviewForm })
  ElMessage.success('审核完成')
  reviewVisible.value = false
  await loadRows()
}

onMounted(loadRows)
</script>

<style scoped>
.page-stack { display: flex; flex-direction: column; gap: 16px; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.toolbar-actions { display: flex; gap: 12px; }
.filter-form { margin-bottom: 16px; }
.pager-wrap { display: flex; justify-content: flex-end; margin-top: 16px; }
.upload-tip { color: #909399; font-size: 12px; }
</style>
