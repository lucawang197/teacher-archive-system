<template>
  <div class="page-stack">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="10" v-if="isAdmin">
        <el-card>
          <template #header>
            <div class="section-header">
              <span>评分模板</span>
              <el-button type="primary" @click="openTemplateDialog">新建模板</el-button>
            </div>
          </template>
          <el-table :data="templates" empty-text="暂无模板">
            <el-table-column prop="templateName" label="模板名称" min-width="160" />
            <el-table-column prop="schoolYear" label="学年" width="120" />
            <el-table-column prop="subjectName" label="适用学科" width="120" />
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.active ? 'success' : 'info'">{{ scope.row.active ? '已生效' : '未生效' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button text type="primary" @click="activateTemplate(scope.row)">生效</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="isAdmin ? 14 : 24">
        <el-card>
          <template #header>
            <div class="section-header">
              <span>{{ isAdmin ? '考评结果管理' : '我的考评结果' }}</span>
              <div class="toolbar-actions">
                <el-select v-if="isAdmin" v-model="scoringForm.templateId" placeholder="选择模板" style="width: 180px">
                  <el-option v-for="item in templates" :key="item.id" :label="item.templateName" :value="item.id" />
                </el-select>
                <el-button v-if="isAdmin" type="primary" @click="runScoring">执行评分</el-button>
                <el-button v-if="isAdmin" @click="handleExport">导出结果</el-button>
              </div>
            </div>
          </template>

          <el-table :data="displayResults" empty-text="暂无评分结果">
            <el-table-column label="教师" min-width="120">
              <template #default="scope">{{ scope.row.teacher?.realName || '-' }}</template>
            </el-table-column>
            <el-table-column prop="totalScore" label="综合得分" width="120" sortable />
            <el-table-column prop="rankingNo" label="排名" width="100" sortable />
            <el-table-column prop="scoreTime" label="评分时间" min-width="180" />
            <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
            <el-table-column label="缺失项" min-width="180">
              <template #default="scope">{{ formatMissing(scope.row.missingItemsJson) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="140" v-if="isAdmin">
              <template #default="scope">
                <el-button text type="primary" @click="openRemarkDialog(scope.row)">备注</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="templateVisible" title="新建评分模板" width="760px">
      <el-form :model="templateForm" label-width="90px">
        <el-form-item label="模板名称">
          <el-input v-model="templateForm.templateName" />
        </el-form-item>
        <el-form-item label="学年">
          <el-input v-model="templateForm.schoolYear" placeholder="如 2025-2026" />
        </el-form-item>
        <el-form-item label="适用学科">
          <el-input v-model="templateForm.subjectName" placeholder="留空表示全部学科" />
        </el-form-item>
        <el-divider content-position="left">指标配置</el-divider>
        <div v-for="(indicator, index) in templateForm.indicators" :key="index" class="indicator-card">
          <el-row :gutter="12">
            <el-col :span="6"><el-input v-model="indicator.indicatorCode" placeholder="指标编码" /></el-col>
            <el-col :span="6"><el-input v-model="indicator.indicatorName" placeholder="指标名称" /></el-col>
            <el-col :span="4"><el-input-number v-model="indicator.weight" :min="0" :max="100" style="width:100%" /></el-col>
            <el-col :span="6"><el-input v-model="indicator.scoreRuleJson" placeholder='评分规则 JSON' /></el-col>
            <el-col :span="2"><el-button text type="danger" @click="removeIndicator(index)">删除</el-button></el-col>
          </el-row>
        </div>
        <el-button @click="addIndicator">新增指标</el-button>
      </el-form>
      <template #footer>
        <el-button @click="templateVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTemplate">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="remarkVisible" title="编辑备注" width="480px">
      <el-input v-model="remarkForm.remark" type="textarea" :rows="4" maxlength="100" show-word-limit />
      <template #footer>
        <el-button @click="remarkVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRemark">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http, { downloadFile } from '../api/http'

const isAdmin = computed(() => localStorage.getItem('role') === 'ADMIN')
const userId = Number(localStorage.getItem('userId') || 0)
const templates = ref([])
const results = ref([])
const displayResults = ref([])
const templateVisible = ref(false)
const remarkVisible = ref(false)
const currentResultId = ref(null)
const scoringForm = reactive({ templateId: undefined })
const remarkForm = reactive({ remark: '' })
const templateForm = reactive({
  templateName: '',
  schoolYear: '',
  subjectName: '',
  indicators: [
    { indicatorCode: 'OPEN_CLASS', indicatorName: '公开课', weight: 25, scoreRuleJson: '{"sourceField":"openClassCount","unitScore":10,"maxScore":100}', sortOrder: 1 },
    { indicatorCode: 'PAPER', indicatorName: '论文', weight: 25, scoreRuleJson: '{"sourceField":"paperCount","unitScore":15,"maxScore":100}', sortOrder: 2 },
  ],
})

const loadTemplates = async () => {
  const res = await http.get('/evaluations/templates')
  templates.value = res.data || []
  if (!scoringForm.templateId) {
    scoringForm.templateId = templates.value.find((item) => item.active)?.id || templates.value[0]?.id
  }
}

const loadResults = async () => {
  const params = isAdmin.value ? {} : { teacherId: userId }
  const res = await http.get('/evaluations/results', { params })
  results.value = res.data || []
  displayResults.value = results.value
}

const openTemplateDialog = () => {
  templateVisible.value = true
}

const addIndicator = () => {
  templateForm.indicators.push({ indicatorCode: '', indicatorName: '', weight: 0, scoreRuleJson: '{"sourceField":"paperCount","unitScore":10,"maxScore":100}', sortOrder: templateForm.indicators.length + 1 })
}

const removeIndicator = (index) => {
  templateForm.indicators.splice(index, 1)
}

const submitTemplate = async () => {
  templateForm.indicators.forEach((item, index) => { item.sortOrder = index + 1 })
  await http.post('/evaluations/templates', templateForm)
  ElMessage.success('模板创建成功')
  templateVisible.value = false
  await loadTemplates()
}

const activateTemplate = async (row) => {
  await http.put(`/evaluations/templates/${row.id}/activate`)
  ElMessage.success('模板已生效')
  await loadTemplates()
}

const runScoring = async () => {
  await http.post('/evaluations/run', { templateId: scoringForm.templateId })
  ElMessage.success('评分成功')
  await loadResults()
}

const handleExport = async () => {
  await downloadFile('/exports/results', `教师考评结果_${new Date().toISOString().slice(0, 10)}.xlsx`)
}

const formatMissing = (json) => {
  if (!json) return '-'
  try {
    const arr = JSON.parse(json)
    return arr.length ? arr.join('、') : '-'
  } catch {
    return json
  }
}

const openRemarkDialog = (row) => {
  currentResultId.value = row.id
  remarkForm.remark = row.remark || ''
  remarkVisible.value = true
}

const submitRemark = async () => {
  await http.put(`/evaluations/results/${currentResultId.value}/remark`, { remark: remarkForm.remark })
  ElMessage.success('备注更新成功')
  remarkVisible.value = false
  await loadResults()
}

onMounted(async () => {
  await loadTemplates()
  await loadResults()
})
</script>

<style scoped>
.page-stack { display: flex; flex-direction: column; gap: 16px; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.toolbar-actions { display: flex; gap: 12px; flex-wrap: wrap; }
.indicator-card { padding: 12px; background: #f5f7fa; border-radius: 14px; margin-bottom: 12px; }
</style>
