<template>
  <div class="dashboard-content">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon weight">
              <el-icon><Scale /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">当前体重</div>
              <div class="stat-value">{{ latestWeight?.weight || '--' }} kg</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon bmi">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">BMI</div>
              <div class="stat-value">{{ latestWeight?.bmi || '--' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon target">
              <el-icon><Flag /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">目标体重</div>
              <div class="stat-value">{{ userStore.user?.targetWeight || '--' }} kg</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon progress">
              <el-icon><Aim /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">距离目标</div>
              <div class="stat-value">{{ weightDiff }} kg</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>体重趋势</span>
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="fetchWeightTrend"
                style="width: 280px"
              />
            </div>
          </template>
          <div ref="weightChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>今日统计</span>
          </template>
          <div class="today-stats">
            <div class="stat-item">
              <el-icon color="#67C23A"><Food /></el-icon>
              <span class="label">摄入卡路里</span>
              <span class="value">{{ todayCalories }} kcal</span>
            </div>
            <el-divider />
            <div class="stat-item">
              <el-icon color="#E6A23C"><Bicycle /></el-icon>
              <span class="label">消耗卡路里</span>
              <span class="value">{{ todayBurned }} kcal</span>
            </div>
            <el-divider />
            <div class="stat-item">
              <el-icon color="#409EFF"><TrendCharts /></el-icon>
              <span class="label">净摄入</span>
              <span class="value">{{ netCalories }} kcal</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import { useWeightStore } from '@/store/modules/weight'
import { getLatestWeight } from '@/api/weight'
import { getDailyCalories } from '@/api/diet'
import { getDailyCaloriesBurned } from '@/api/exercise'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

const userStore = useUserStore()
const weightStore = useWeightStore()
const latestWeight = ref(null)
const todayCalories = ref(0)
const todayBurned = ref(0)
const weightChartRef = ref(null)
const dateRange = ref([
  dayjs().subtract(30, 'day').toDate(),
  dayjs().toDate()
])

const weightDiff = computed(() => {
  if (!latestWeight.value?.weight || !userStore.user?.targetWeight) return '--'
  const diff = latestWeight.value.weight - userStore.user.targetWeight
  return diff.toFixed(2)
})

const netCalories = computed(() => {
  return (todayCalories.value - todayBurned.value).toFixed(0)
})

const fetchLatestWeight = async () => {
  try {
    const res = await getLatestWeight()
    latestWeight.value = res.data
  } catch (error) {
    console.error('Failed to fetch latest weight:', error)
  }
}

const fetchTodayStats = async () => {
  const today = dayjs().format('YYYY-MM-DD')
  try {
    const [caloriesRes, burnedRes] = await Promise.all([
      getDailyCalories(today),
      getDailyCaloriesBurned(today)
    ])
    todayCalories.value = caloriesRes.data || 0
    todayBurned.value = burnedRes.data || 0
  } catch (error) {
    console.error('Failed to fetch today stats:', error)
  }
}

const fetchWeightTrend = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) return

  const startDate = dayjs(dateRange.value[0]).format('YYYY-MM-DD')
  const endDate = dayjs(dateRange.value[1]).format('YYYY-MM-DD')

  try {
    await weightStore.fetchTrend(startDate, endDate)
    renderWeightChart()
  } catch (error) {
    console.error('Failed to fetch weight trend:', error)
  }
}

const renderWeightChart = () => {
  if (!weightChartRef.value) return

  const chart = echarts.init(weightChartRef.value)
  const data = weightStore.trendData || []

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: data.map(item => dayjs(item.recordDate).format('MM-DD'))
    },
    yAxis: {
      type: 'value',
      name: '体重 (kg)'
    },
    series: [
      {
        name: '体重',
        type: 'line',
        data: data.map(item => item.weight),
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }

  chart.setOption(option)
}

onMounted(async () => {
  await fetchLatestWeight()
  await fetchTodayStats()
  await fetchWeightTrend()
  await nextTick()
  renderWeightChart()
})
</script>

<style scoped>
.dashboard-content {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  color: white;
}

.stat-icon.weight {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.bmi {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.target {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.progress {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.today-stats {
  padding: 10px 0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
}

.stat-item .label {
  flex: 1;
  color: #606266;
}

.stat-item .value {
  font-weight: bold;
  color: #303133;
}
</style>
