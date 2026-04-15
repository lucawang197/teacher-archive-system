import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
  baseURL: '/api',
  timeout: 20000,
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

http.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (payload && typeof payload === 'object') {
      if ('success' in payload) {
        if (!payload.success) {
          ElMessage.error(payload.message || '请求失败')
          return Promise.reject(new Error(payload.message || '请求失败'))
        }
        return payload
      }
      if ('code' in payload) {
        if (payload.code !== 200) {
          ElMessage.error(payload.message || '请求失败')
          return Promise.reject(new Error(payload.message || '请求失败'))
        }
        return payload
      }
    }
    return response
  },
  (error) => {
    const status = error?.response?.status
    const message = error?.response?.data?.message || error?.message || '请求失败'
    if (status === 401) {
      localStorage.clear()
      if (window.location.pathname !== '/login') {
        ElMessage.error('登录已过期，请重新登录')
        window.location.href = '/login'
      }
    } else {
      ElMessage.error(message)
    }
    return Promise.reject(error)
  }
)

function buildApiUrl(url) {
  return url.startsWith('/api') ? url : `/api${url}`
}

export async function downloadFile(url, filename) {
  const token = localStorage.getItem('token')
  const response = await axios.get(buildApiUrl(url), {
    responseType: 'blob',
    timeout: 30000,
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  })
  const blob = new Blob([response.data])
  const objectUrl = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = objectUrl
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(objectUrl)
}

export async function openFile(url) {
  const token = localStorage.getItem('token')
  const response = await axios.get(buildApiUrl(url), {
    responseType: 'blob',
    timeout: 30000,
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  })
  const blob = new Blob([response.data])
  const objectUrl = URL.createObjectURL(blob)
  window.open(objectUrl, '_blank')
  setTimeout(() => URL.revokeObjectURL(objectUrl), 60000)
}

export default http
