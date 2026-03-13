<template>
  <div class="login-container">
    <!-- 装饰性背景元素 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="login-content">
      <div class="login-header">
        <div class="logo-wrapper">
          <el-icon :size="48" class="logo-icon">
            <TrendCharts />
          </el-icon>
        </div>
        <h1 class="login-title">减重管理系统</h1>
        <p class="login-subtitle">科学管理 · 健康生活 · 轻松达标</p>
      </div>

      <el-card class="login-card" shadow="always">
        <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-position="top">
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              prefix-icon="User"
              clearable
            />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="handleLogin"
              :loading="loading"
              size="large"
              class="login-button"
            >
              <span v-if="!loading">立即登录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>
          <div class="login-footer">
            <el-link type="primary" @click="goToRegister">
              <el-icon><UserFilled /></el-icon>
              还没有账号？立即注册
            </el-link>
          </div>
        </el-form>
      </el-card>

      <!-- 备案信息 -->
      <div class="beian-info">
        <p class="beian-text">
          <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener noreferrer">
            京ICP备2024000000号
          </a>
        </p>
        <p class="copyright">
          © 2025 减重管理系统 · All Rights Reserved
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(loginForm.value)
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: auto;
  min-height: 100vh;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: -50px;
  animation-delay: 5s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 10%;
  animation-delay: 10s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

.login-content {
  position: relative;
  width: 100%;
  max-width: 440px;
  padding: 20px;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  color: white;
}

.logo-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  margin-bottom: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.logo-icon {
  color: white;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.login-title {
  font-size: 38px;
  font-weight: 700;
  margin: 0 0 12px 0;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  letter-spacing: 1px;
}

.login-subtitle {
  font-size: 15px;
  margin: 0;
  opacity: 0.95;
  font-weight: 300;
  letter-spacing: 2px;
}

.login-card {
  border-radius: 16px;
  padding: 30px;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.98);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.login-card :deep(.el-form-item__label) {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.login-card :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.login-card :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(102, 126, 234, 0.15);
}

.login-card :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 2px 12px rgba(102, 126, 234, 0.25);
}

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  text-align: center;
  margin-top: 16px;
}

.login-footer :deep(.el-link) {
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

/* 备案信息 */
.beian-info {
  text-align: center;
  margin-top: 30px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
}

.beian-text {
  margin: 0 0 8px 0;
}

.beian-text a {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  transition: all 0.3s;
  padding: 4px 8px;
  border-radius: 4px;
}

.beian-text a:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.copyright {
  margin: 0;
  opacity: 0.8;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-content {
    max-width: 100%;
    padding: 15px;
  }

  .login-title {
    font-size: 32px;
  }

  .login-subtitle {
    font-size: 14px;
  }

  .login-card {
    padding: 24px 20px;
  }

  .logo-wrapper {
    width: 70px;
    height: 70px;
  }

  .logo-icon {
    font-size: 40px;
  }
}
</style>
