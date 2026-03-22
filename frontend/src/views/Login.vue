<template>
  <div class="login-page">
    <!-- 左侧品牌区域 -->
    <div class="brand-section">
      <div class="brand-content">
        <!-- Logo -->
        <div class="brand-logo">
          <span class="logo-icon">🌿</span>
          <span class="logo-text">Vitality Flow</span>
        </div>

        <!-- 主标题 -->
        <h1 class="brand-title">
          编织您的<br />
          <span class="highlight">健康叙事</span>
        </h1>

        <!-- 描述 -->
        <p class="brand-desc">
          通过智能分析与个性化建议，重新定义您的活力状态。每一个数据点都是您生命故事的新篇章。
        </p>

        <!-- 今日洞察卡片 -->
        <div class="insight-card">
          <div class="insight-header">
            <span class="insight-icon">💡</span>
            <span class="insight-label">今日洞察</span>
          </div>
          <p class="insight-text">您的基础代谢水平提升了 <strong>12%</strong></p>
        </div>
      </div>

      <!-- 装饰性背景 -->
      <div class="brand-bg-decoration">
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>
      </div>
    </div>

    <!-- 右侧登录区域 -->
    <div class="login-section">
      <div class="login-wrapper">
        <!-- 登录表单头部 -->
        <div class="login-header">
          <div class="login-logo">
            <span class="logo-icon">🌿</span>
            <span class="logo-text">Vitality Flow</span>
          </div>
          <h2 class="login-title">欢迎回来</h2>
          <p class="login-subtitle">登录您的账户以继续管理您的健康旅程</p>
        </div>

        <!-- 登录表单 -->
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="rules"
          class="login-form"
          @submit.prevent="handleLogin"
        >
          <!-- 账号输入 -->
          <el-form-item prop="username">
            <div class="input-wrapper">
              <label class="input-label">账号 / 邮箱地址</label>
              <el-input
                v-model="loginForm.username"
                placeholder="请输入账号或邮箱"
                size="large"
                class="custom-input"
              >
                <template #prefix>
                  <span class="input-icon">👤</span>
                </template>
              </el-input>
            </div>
          </el-form-item>

          <!-- 密码输入 -->
          <el-form-item prop="password">
            <div class="input-wrapper">
              <label class="input-label">密码</label>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
                class="custom-input"
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <span class="input-icon">🔒</span>
                </template>
              </el-input>
            </div>
          </el-form-item>

          <!-- 记住我 & 忘记密码 -->
          <div class="form-options">
            <el-checkbox v-model="rememberMe" class="remember-checkbox">
              记住我
            </el-checkbox>
            <a href="#" class="forgot-link" @click.prevent="handleForgotPassword">
              忘记密码？
            </a>
          </div>

          <!-- 登录按钮 -->
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="login-btn"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录账户' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 分割线 -->
        <div class="divider">
          <span class="divider-line"></span>
          <span class="divider-text">或者通过以下方式</span>
          <span class="divider-line"></span>
        </div>

        <!-- 社交登录 -->
        <div class="social-login">
          <button class="social-btn wechat" @click="handleSocialLogin('wechat')">
            <span class="social-icon">💬</span>
            <span class="social-label">微信</span>
          </button>
          <button class="social-btn qq" @click="handleSocialLogin('qq')">
            <span class="social-icon">🐧</span>
            <span class="social-label">QQ</span>
          </button>
        </div>

        <!-- 注册链接 -->
        <div class="register-link">
          <span class="register-text">还没有账户？</span>
          <a href="#" class="register-action" @click.prevent="goToRegister">
            立即注册
          </a>
        </div>
      </div>

      <!-- 页脚 -->
      <footer class="login-footer">
        <div class="footer-brand">Vitality Flow</div>
        <div class="footer-links">
          <a href="#">隐私政策</a>
          <span class="footer-divider">|</span>
          <a href="#">服务条款</a>
          <span class="footer-divider">|</span>
          <a href="#">联系支持</a>
        </div>
        <div class="footer-copyright">
          © 2024 Vitality Flow. 保留所有权利。
        </div>
      </footer>
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
const rememberMe = ref(false)

const loginForm = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入账号或邮箱', trigger: 'blur' },
    { min: 3, max: 50, message: '账号长度在 3 到 50 个字符', trigger: 'blur' }
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

const handleForgotPassword = () => {
  ElMessage.info('请联系管理员重置密码')
}

const handleSocialLogin = (type) => {
  ElMessage.info(`${type === 'wechat' ? '微信' : 'QQ'}登录功能开发中`)
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
/* ==================== 设计系统变量 ==================== */
.login-page {
  --primary: #2D6A4F;
  --primary-light: #95d4b3;
  --primary-dark: #0f5238;
  --surface: #f7f9fb;
  --surface-low: #f2f4f6;
  --surface-lowest: #ffffff;
  --on-surface: #191c1e;
  --on-surface-variant: #404943;
  --outline-variant: #bfc9c1;
  --radius-lg: 24px;
  --radius-md: 12px;

  display: flex;
  min-height: 100vh;
  font-family: 'Lexend', -apple-system, BlinkMacSystemFont, sans-serif;
}

/* ==================== 左侧品牌区域 ==================== */
.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #0f5238 0%, #2D6A4F 50%, #3d8b68 100%);
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.brand-content {
  position: relative;
  z-index: 2;
  max-width: 480px;
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 48px;
}

.brand-logo .logo-icon {
  font-size: 28px;
}

.brand-logo .logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #ffffff;
  letter-spacing: 0.02em;
}

.brand-title {
  font-family: 'Manrope', sans-serif;
  font-size: 48px;
  font-weight: 800;
  color: #ffffff;
  line-height: 1.2;
  margin: 0 0 24px 0;
  letter-spacing: -0.02em;
}

.brand-title .highlight {
  color: #95d4b3;
}

.brand-desc {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
  line-height: 1.7;
  margin: 0 0 40px 0;
}

/* 洞察卡片 */
.insight-card {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(12px);
  border-radius: var(--radius-lg);
  padding: 24px;
  border: 1px solid rgba(255, 255, 255, 0.15);
}

.insight-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.insight-icon {
  font-size: 20px;
}

.insight-label {
  font-size: 12px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.7);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.insight-text {
  font-size: 16px;
  color: #ffffff;
  margin: 0;
}

.insight-text strong {
  color: #95d4b3;
  font-weight: 700;
}

/* 装饰背景 */
.brand-bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
}

.deco-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  right: -100px;
}

.deco-2 {
  width: 300px;
  height: 300px;
  bottom: 10%;
  right: 20%;
}

.deco-3 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  left: -50px;
}

/* ==================== 右侧登录区域 ==================== */
.login-section {
  width: 520px;
  background: var(--surface-lowest);
  display: flex;
  flex-direction: column;
  padding: 48px 60px;
}

.login-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 登录头部 */
.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.login-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-bottom: 24px;
}

.login-logo .logo-icon {
  font-size: 24px;
}

.login-logo .logo-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--primary);
}

.login-title {
  font-family: 'Manrope', sans-serif;
  font-size: 28px;
  font-weight: 700;
  color: var(--on-surface);
  margin: 0 0 8px 0;
}

.login-subtitle {
  font-size: 14px;
  color: var(--on-surface-variant);
  margin: 0;
}

/* 表单样式 */
.login-form {
  width: 100%;
}

.input-wrapper {
  width: 100%;
}

.input-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: var(--on-surface);
  margin-bottom: 8px;
}

.custom-input :deep(.el-input__wrapper) {
  background: var(--surface-low);
  border: none;
  border-radius: var(--radius-md);
  box-shadow: none;
  padding: 4px 16px;
  height: 48px;
  transition: all 0.2s ease;
}

.custom-input :deep(.el-input__wrapper:hover) {
  background: #e8eaec;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  background: var(--surface-lowest);
  box-shadow: 0 0 0 2px var(--primary);
}

.custom-input :deep(.el-input__inner) {
  font-size: 15px;
  color: var(--on-surface);
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: #707973;
}

.input-icon {
  font-size: 18px;
  margin-right: 4px;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-checkbox :deep(.el-checkbox__label) {
  font-size: 14px;
  color: var(--on-surface-variant);
}

.remember-checkbox :deep(.el-checkbox__inner) {
  border-color: var(--outline-variant);
}

.remember-checkbox :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: var(--primary);
  border-color: var(--primary);
}

.forgot-link {
  font-size: 14px;
  color: var(--primary);
  text-decoration: none;
  font-weight: 500;
}

.forgot-link:hover {
  text-decoration: underline;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 26px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(45, 106, 79, 0.35);
}

/* 分割线 */
.divider {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 28px 0;
}

.divider-line {
  flex: 1;
  height: 1px;
  background: #e6e8ea;
}

.divider-text {
  font-size: 13px;
  color: #707973;
  white-space: nowrap;
}

/* 社交登录 */
.social-login {
  display: flex;
  gap: 16px;
}

.social-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 48px;
  background: var(--surface-low);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s ease;
}

.social-btn:hover {
  background: #e6e8ea;
}

.social-icon {
  font-size: 20px;
}

.social-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--on-surface);
}

.social-btn.wechat:hover {
  background: #f0fdf4;
}

.social-btn.qq:hover {
  background: #eff6ff;
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 32px;
}

.register-text {
  font-size: 14px;
  color: var(--on-surface-variant);
}

.register-action {
  font-size: 14px;
  font-weight: 600;
  color: var(--primary);
  text-decoration: none;
  margin-left: 4px;
}

.register-action:hover {
  text-decoration: underline;
}

/* 页脚 */
.login-footer {
  text-align: center;
  padding-top: 32px;
  border-top: 1px solid #e6e8ea;
}

.footer-brand {
  font-weight: 600;
  color: var(--on-surface);
  margin-bottom: 12px;
}

.footer-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.footer-links a {
  font-size: 13px;
  color: var(--on-surface-variant);
  text-decoration: none;
}

.footer-links a:hover {
  color: var(--primary);
}

.footer-divider {
  color: #bfc9c1;
}

.footer-copyright {
  font-size: 12px;
  color: #707973;
}

/* ==================== 响应式 ==================== */
@media (max-width: 1024px) {
  .brand-section {
    padding: 40px;
  }

  .brand-title {
    font-size: 40px;
  }

  .login-section {
    width: 460px;
    padding: 40px 48px;
  }
}

@media (max-width: 900px) {
  .login-page {
    flex-direction: column;
  }

  .brand-section {
    padding: 40px;
    min-height: auto;
  }

  .brand-title {
    font-size: 36px;
  }

  .insight-card {
    display: none;
  }

  .login-section {
    width: 100%;
    padding: 40px 24px;
  }
}

@media (max-width: 480px) {
  .brand-section {
    padding: 32px 24px;
  }

  .brand-title {
    font-size: 28px;
  }

  .brand-desc {
    font-size: 14px;
  }

  .login-logo {
    display: none;
  }

  .login-title {
    font-size: 24px;
  }

  .social-login {
    flex-direction: column;
  }
}
</style>
