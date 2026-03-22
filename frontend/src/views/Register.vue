<template>
  <div class="register-page">
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
          开启您的<br />
          <span class="highlight">健康进化之旅</span>
        </h1>

        <!-- 描述 -->
        <p class="brand-desc">
          加入我们的社区，与数千名用户一起，通过科学的方法和智能分析，实现您的健康目标。
        </p>

        <!-- 统计数据 -->
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-value">15k+</div>
            <div class="stat-label">活跃用户</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">98%</div>
            <div class="stat-label">满意度</div>
          </div>
        </div>
      </div>

      <!-- 装饰性背景 -->
      <div class="brand-bg-decoration">
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>
      </div>
    </div>

    <!-- 右侧注册区域 -->
    <div class="register-section">
      <div class="register-wrapper">
        <!-- 注册表单头部 -->
        <div class="register-header">
          <div class="register-logo">
            <span class="logo-icon">🌿</span>
            <span class="logo-text">Vitality Flow</span>
          </div>
          <h2 class="register-title">创建新账号</h2>
          <p class="register-subtitle">填写以下信息开始您的健康旅程</p>
        </div>

        <!-- 注册表单 -->
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="rules"
          class="register-form"
          @submit.prevent="handleRegister"
        >
          <!-- 用户名和昵称 -->
          <div class="form-row">
            <el-form-item prop="username" class="form-item-half">
              <div class="input-wrapper">
                <label class="input-label">用户名</label>
                <el-input
                  v-model="registerForm.username"
                  placeholder="请输入用户名"
                  size="large"
                  class="custom-input"
                >
                  <template #prefix>
                    <span class="input-icon">👤</span>
                  </template>
                </el-input>
              </div>
            </el-form-item>
            <el-form-item prop="nickname" class="form-item-half">
              <div class="input-wrapper">
                <label class="input-label">昵称</label>
                <el-input
                  v-model="registerForm.nickname"
                  placeholder="请输入昵称"
                  size="large"
                  class="custom-input"
                >
                  <template #prefix>
                    <span class="input-icon">✨</span>
                  </template>
                </el-input>
              </div>
            </el-form-item>
          </div>

          <!-- 邮箱 -->
          <el-form-item prop="email">
            <div class="input-wrapper">
              <label class="input-label">邮箱地址</label>
              <el-input
                v-model="registerForm.email"
                placeholder="请输入邮箱"
                size="large"
                class="custom-input"
              >
                <template #prefix>
                  <span class="input-icon">📧</span>
                </template>
              </el-input>
            </div>
          </el-form-item>

          <!-- 密码和确认密码 -->
          <div class="form-row">
            <el-form-item prop="password" class="form-item-half">
              <div class="input-wrapper">
                <label class="input-label">密码</label>
                <el-input
                  v-model="registerForm.password"
                  type="password"
                  placeholder="至少6个字符"
                  size="large"
                  show-password
                  class="custom-input"
                >
                  <template #prefix>
                    <span class="input-icon">🔒</span>
                  </template>
                </el-input>
              </div>
            </el-form-item>
            <el-form-item prop="confirmPassword" class="form-item-half">
              <div class="input-wrapper">
                <label class="input-label">确认密码</label>
                <el-input
                  v-model="registerForm.confirmPassword"
                  type="password"
                  placeholder="请再次输入密码"
                  size="large"
                  show-password
                  class="custom-input"
                >
                  <template #prefix>
                    <span class="input-icon">🔐</span>
                  </template>
                </el-input>
              </div>
            </el-form-item>
          </div>

          <!-- 性别、身高、目标体重 -->
          <div class="form-row three-cols">
            <el-form-item prop="gender" class="form-item-third">
              <div class="input-wrapper">
                <label class="input-label">性别</label>
                <div class="gender-group">
                  <button
                    type="button"
                    :class="['gender-btn', { active: registerForm.gender === '男' }]"
                    @click="registerForm.gender = '男'"
                  >
                    <span class="gender-icon">👨</span>
                    <span>男</span>
                  </button>
                  <button
                    type="button"
                    :class="['gender-btn', { active: registerForm.gender === '女' }]"
                    @click="registerForm.gender = '女'"
                  >
                    <span class="gender-icon">👩</span>
                    <span>女</span>
                  </button>
                </div>
              </div>
            </el-form-item>
            <el-form-item prop="height" class="form-item-third">
              <div class="input-wrapper">
                <label class="input-label">身高 (cm)</label>
                <div class="stepper-wrapper">
                  <button type="button" class="stepper-btn" @click="decrementHeight">−</button>
                  <span class="stepper-value">{{ registerForm.height || 170 }}</span>
                  <button type="button" class="stepper-btn" @click="incrementHeight">+</button>
                </div>
              </div>
            </el-form-item>
            <el-form-item prop="targetWeight" class="form-item-third">
              <div class="input-wrapper">
                <label class="input-label">目标体重 (kg)</label>
                <div class="stepper-wrapper">
                  <button type="button" class="stepper-btn" @click="decrementWeight">−</button>
                  <span class="stepper-value">{{ registerForm.targetWeight || 65 }}</span>
                  <button type="button" class="stepper-btn" @click="incrementWeight">+</button>
                </div>
              </div>
            </el-form-item>
          </div>

          <!-- 注册按钮 -->
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="register-btn"
              @click="handleRegister"
            >
              {{ loading ? '注册中...' : '立即注册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 分割线 -->
        <div class="divider">
          <span class="divider-line"></span>
          <span class="divider-text">或者</span>
          <span class="divider-line"></span>
        </div>

        <!-- 社交登录 -->
        <div class="social-login">
          <button class="social-btn wechat" @click="handleSocialLogin('wechat')">
            <span class="social-icon">💬</span>
            <span class="social-label">微信注册</span>
          </button>
          <button class="social-btn qq" @click="handleSocialLogin('qq')">
            <span class="social-icon">🐧</span>
            <span class="social-label">QQ注册</span>
          </button>
        </div>

        <!-- 登录链接 -->
        <div class="login-link">
          <span class="login-text">已有账户？</span>
          <a href="#" class="login-action" @click.prevent="goToLogin">
            立即登录
          </a>
        </div>
      </div>

      <!-- 页脚 -->
      <footer class="register-footer">
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

const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  nickname: '',
  gender: '',
  height: 170,
  targetWeight: 65
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 身高步进器
const incrementHeight = () => {
  if (registerForm.value.height < 250) {
    registerForm.value.height++
  }
}

const decrementHeight = () => {
  if (registerForm.value.height > 100) {
    registerForm.value.height--
  }
}

// 体重步进器
const incrementWeight = () => {
  if (registerForm.value.targetWeight < 200) {
    registerForm.value.targetWeight = Number((registerForm.value.targetWeight + 0.5).toFixed(1))
  }
}

const decrementWeight = () => {
  if (registerForm.value.targetWeight > 30) {
    registerForm.value.targetWeight = Number((registerForm.value.targetWeight - 0.5).toFixed(1))
  }
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { confirmPassword, ...data } = registerForm.value
        await userStore.register(data)
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleSocialLogin = (type) => {
  ElMessage.info(`${type === 'wechat' ? '微信' : 'QQ'}注册功能开发中`)
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
/* ==================== 设计系统变量 ==================== */
.register-page {
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

/* 统计数据 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(12px);
  border-radius: var(--radius-lg);
  padding: 24px;
  border: 1px solid rgba(255, 255, 255, 0.15);
}

.stat-value {
  font-family: 'Manrope', sans-serif;
  font-size: 32px;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
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

/* ==================== 右侧注册区域 ==================== */
.register-section {
  width: 560px;
  background: var(--surface-lowest);
  display: flex;
  flex-direction: column;
  padding: 40px 60px;
  overflow-y: auto;
}

.register-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 注册头部 */
.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-bottom: 20px;
}

.register-logo .logo-icon {
  font-size: 24px;
}

.register-logo .logo-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--primary);
}

.register-title {
  font-family: 'Manrope', sans-serif;
  font-size: 28px;
  font-weight: 700;
  color: var(--on-surface);
  margin: 0 0 8px 0;
}

.register-subtitle {
  font-size: 14px;
  color: var(--on-surface-variant);
  margin: 0;
}

/* 表单样式 */
.register-form {
  width: 100%;
}

.form-row {
  display: flex;
  gap: 16px;
  margin-bottom: 0;
}

.form-row.three-cols {
  gap: 12px;
}

.form-item-half {
  flex: 1;
}

.form-item-third {
  flex: 1;
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

/* 性别选择 */
.gender-group {
  display: flex;
  gap: 8px;
}

.gender-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  height: 48px;
  background: var(--surface-low);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: var(--on-surface);
  transition: all 0.2s ease;
}

.gender-btn:hover {
  background: #e8eaec;
}

.gender-btn.active {
  background: var(--primary);
  color: #ffffff;
}

.gender-icon {
  font-size: 18px;
}

/* 步进器 */
.stepper-wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 48px;
  background: var(--surface-low);
  border-radius: var(--radius-md);
  padding: 0 8px;
}

.stepper-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--surface-lowest);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
  font-weight: 500;
  color: var(--on-surface);
  transition: all 0.2s ease;
}

.stepper-btn:hover {
  background: var(--primary);
  color: #ffffff;
}

.stepper-value {
  font-size: 16px;
  font-weight: 600;
  color: var(--on-surface);
  min-width: 60px;
  text-align: center;
}

/* 注册按钮 */
.register-btn {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 26px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  border: none;
  transition: all 0.3s ease;
  margin-top: 8px;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(45, 106, 79, 0.35);
}

/* 分割线 */
.divider {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 24px 0;
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

/* 登录链接 */
.login-link {
  text-align: center;
  margin-top: 28px;
}

.login-text {
  font-size: 14px;
  color: var(--on-surface-variant);
}

.login-action {
  font-size: 14px;
  font-weight: 600;
  color: var(--primary);
  text-decoration: none;
  margin-left: 4px;
}

.login-action:hover {
  text-decoration: underline;
}

/* 页脚 */
.register-footer {
  text-align: center;
  padding-top: 28px;
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

  .register-section {
    width: 480px;
    padding: 36px 40px;
  }
}

@media (max-width: 900px) {
  .register-page {
    flex-direction: column;
  }

  .brand-section {
    padding: 40px;
    min-height: auto;
  }

  .brand-title {
    font-size: 36px;
  }

  .stats-grid {
    display: none;
  }

  .register-section {
    width: 100%;
    padding: 36px 24px;
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .form-row.three-cols {
    flex-direction: row;
    gap: 8px;
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

  .register-logo {
    display: none;
  }

  .register-title {
    font-size: 24px;
  }

  .form-row.three-cols {
    flex-direction: column;
    gap: 0;
  }

  .social-login {
    flex-direction: column;
  }
}
</style>
