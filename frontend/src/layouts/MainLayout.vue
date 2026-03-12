<template>
  <div class="main-layout">
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>减重管理系统</h2>
          <div class="user-info">
            <span>欢迎，{{ userStore.user?.nickname || userStore.user?.username }}</span>
            <el-button @click="handleLogout" type="danger" size="small">退出</el-button>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu :default-active="activeMenu" router>
            <el-menu-item index="/dashboard">
              <el-icon><DataLine /></el-icon>
              <span>仪表盘</span>
            </el-menu-item>
            <el-menu-item index="/weight">
              <el-icon><Platform /></el-icon>
              <span>体重管理</span>
            </el-menu-item>
            <el-menu-item index="/diet">
              <el-icon><Food /></el-icon>
              <span>饮食管理</span>
            </el-menu-item>
            <el-menu-item index="/exercise">
              <el-icon><Bicycle /></el-icon>
              <span>运动管理</span>
            </el-menu-item>
            <el-menu-item index="/profile">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.main-layout {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.main-layout .el-container {
  height: 100%;
}

.el-header {
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  height: 60px;
  flex-shrink: 0;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.el-aside {
  background-color: #f5f7fa;
  overflow-y: auto;
}

.el-main {
  background-color: #f0f2f5;
  padding: 0;
  overflow-y: auto;
  height: 100%;
}
</style>