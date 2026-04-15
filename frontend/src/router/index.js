import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '../layouts/AdminLayout.vue'
import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'
import ArchiveCenterView from '../views/ArchiveCenterView.vue'
import DataCenterView from '../views/DataCenterView.vue'
import EvaluationCenterView from '../views/EvaluationCenterView.vue'
import UserCenterView from '../views/UserCenterView.vue'
import OpsCenterView from '../views/OpsCenterView.vue'
import BackupCenterView from '../views/BackupCenterView.vue'
import LogView from '../views/LogView.vue'
import LoginAuditView from '../views/LoginAuditView.vue'

const routes = [
  { path: '/login', name: 'login', component: LoginView, meta: { public: true, title: '登录' } },
  {
    path: '/',
    component: AdminLayout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'dashboard', component: DashboardView, meta: { title: '工作台' } },
      { path: 'archives', name: 'archives', component: ArchiveCenterView, meta: { title: '档案管理' } },
      { path: 'data', name: 'data', component: DataCenterView, meta: { title: '数据中心' } },
      { path: 'evaluation', name: 'evaluation', component: EvaluationCenterView, meta: { title: '考评管理' } },
      { path: 'users', name: 'users', component: UserCenterView, meta: { title: '用户管理', roles: ['ADMIN'] } },
      { path: 'logs', name: 'logs', component: LogView, meta: { title: '操作审计', roles: ['ADMIN'] } },
      { path: 'login-audits', name: 'login-audits', component: LoginAuditView, meta: { title: '登录日志', roles: ['ADMIN'] } },
      { path: 'ops', name: 'ops', component: OpsCenterView, meta: { title: '运维中心', roles: ['ADMIN'] } },
      { path: 'backups', name: 'backups', component: BackupCenterView, meta: { title: '备份中心', roles: ['ADMIN'] } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

router.beforeEach((to, _from, next) => {
  document.title = `教师档案管理考评系统 - ${to.meta?.title || '控制台'}`
  if (to.meta?.public) {
    next()
    return
  }
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  if (!token) {
    next('/login')
    return
  }
  const roles = to.meta?.roles || []
  if (roles.length && !roles.includes(role)) {
    next('/dashboard')
    return
  }
  next()
})

export default router
