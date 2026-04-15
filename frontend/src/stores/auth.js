import { defineStore } from 'pinia'
import http from '../api/http'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    role: localStorage.getItem('role') || '',
    username: localStorage.getItem('username') || '',
    realName: localStorage.getItem('realName') || '',
    userId: Number(localStorage.getItem('userId') || 0),
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token),
    isAdmin: (state) => state.role === 'ADMIN',
    isTeacher: (state) => state.role === 'TEACHER',
  },
  actions: {
    saveSession(payload) {
      this.token = payload.token || ''
      this.role = payload.role || ''
      this.username = payload.username || ''
      this.realName = payload.realName || ''
      this.userId = Number(payload.userId || 0)
      localStorage.setItem('token', this.token)
      localStorage.setItem('role', this.role)
      localStorage.setItem('username', this.username)
      localStorage.setItem('realName', this.realName)
      localStorage.setItem('userId', String(this.userId))
    },
    clearSession() {
      this.$reset()
      localStorage.clear()
    },
    async login(form) {
      const res = await http.post('/auth/login', form)
      this.saveSession(res.data || {})
      return res
    },
  },
})
