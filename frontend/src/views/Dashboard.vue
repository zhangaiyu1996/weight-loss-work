<template>
  <div class="dashboard-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <h1 class="page-title">今日概览</h1>
    </div>

    <!-- Bento Grid 主布局 -->
    <div class="bento-wrapper">
      <!-- 左侧主要区域 -->
      <div class="bento-main">
        <!-- 第一行：卡路里 + 步数 -->
        <div class="bento-row row-top">
          <!-- 卡路里消耗卡片 -->
          <div class="bento-card card-calories">
            <div class="card-label">
              <span class="label-icon">🔥</span>
              <span class="label-text">卡路里消耗</span>
            </div>
            <div class="calories-hero">
              <span class="hero-number">{{ remainingCalories }}</span>
              <span class="hero-label">剩余卡路里</span>
            </div>
            <div class="calories-stats">
              <div class="stat-block">
                <span class="stat-label">已摄入</span>
                <span class="stat-value intake">{{ todayCalories }} kcal</span>
              </div>
              <div class="stat-divider"></div>
              <div class="stat-block">
                <span class="stat-label">已消耗</span>
                <span class="stat-value burn">{{ todayBurned }} kcal</span>
              </div>
            </div>
          </div>

          <!-- 步数记录卡片 -->
          <div class="bento-card card-steps">
            <div class="card-label">
              <span class="label-icon">🏃</span>
              <span class="label-text">步数记录</span>
            </div>
            <div class="steps-hero">
              <span class="hero-number">{{ todaySteps.toLocaleString() }}</span>
            </div>
            <div class="steps-info">
              <div class="progress-track">
                <div class="progress-bar steps" :style="{ width: stepsProgress + '%' }"></div>
              </div>
              <span class="steps-target">目标: {{ stepsTarget.toLocaleString() }} 步 (已完成 {{ stepsProgress }}%)</span>
            </div>
          </div>
        </div>

        <!-- 第二行：体重趋势图表 -->
        <div class="bento-card card-trend">
          <div class="card-header-row">
            <div class="header-left">
              <span class="label-icon">📊</span>
              <span class="card-title">体重趋势</span>
            </div>
            <div class="view-tabs">
              <button class="tab-btn" :class="{ active: trendView === 'week' }" @click="trendView = 'week'">周视图</button>
              <button class="tab-btn" :class="{ active: trendView === 'month' }" @click="trendView = 'month'">月视图</button>
            </div>
          </div>
          <p class="trend-subtitle">过去 {{ trendView === 'week' ? 7 : 30 }} 天变化</p>
          <div ref="weightChartRef" class="chart-area"></div>
        </div>

        <!-- 第三行：优化建议 -->
        <div class="bento-card card-suggestion">
          <div class="card-label">
            <span class="label-icon">💡</span>
            <span class="label-text">优化您的代谢率</span>
          </div>
          <p class="suggestion-desc">
            基于近三天的步数与饮食分析，建议在晚间瑜伽前补充少量优质碳水。
          </p>
          <a href="#" class="suggestion-link">
            查看完整报告
            <span class="link-arrow">→</span>
          </a>
        </div>
      </div>

      <!-- 右侧边栏 -->
      <div class="bento-sidebar">
        <!-- 今日关注 -->
        <div class="bento-card card-focus">
          <div class="card-label">
            <span class="label-icon">📋</span>
            <span class="label-text">今日关注</span>
          </div>
          <div class="focus-list">
            <div class="focus-item" v-for="(item, index) in todayFocus" :key="index">
              <div class="focus-icon-wrap">{{ item.icon }}</div>
              <div class="focus-content">
                <span class="focus-title">{{ item.title }}</span>
                <span class="focus-meta">{{ item.subtitle }}</span>
              </div>
              <span class="focus-check" :class="{ done: item.completed }">
                {{ item.completed ? '✓' : '○' }}
              </span>
            </div>
          </div>
        </div>

        <!-- 体重小卡片 -->
        <div class="bento-card card-weight-mini">
          <div class="card-label">
            <span class="label-icon">⚖️</span>
            <span class="label-text">体重</span>
          </div>
          <div class="weight-display">
            <span class="weight-value">{{ latestWeight?.weight || '--' }}</span>
            <span class="weight-unit">kg</span>
          </div>
          <div class="weight-trend" :class="weightChangeClass">
            <span class="trend-icon">📉</span>
            <span>{{ weightChange >= 0 ? '+' : '' }}{{ weightChange }} kg</span>
          </div>
        </div>

        <!-- 饮水量小卡片 -->
        <div class="bento-card card-water-mini">
          <div class="card-label">
            <span class="label-icon">💧</span>
            <span class="label-text">饮水量</span>
          </div>
          <div class="water-display">
            <span class="water-value">{{ todayWater }}</span>
            <span class="water-unit">ml</span>
          </div>
          <div class="water-progress">
            <div class="progress-track small">
              <div class="progress-bar water" :style="{ width: waterProgress + '%' }"></div>
            </div>
            <span class="water-percent">{{ waterProgress }}% 目标</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useUserStore } from '@/store/modules/user'
import { useWeightStore } from '@/store/modules/weight'
import { getLatestWeight } from '@/api/weight'
import { getDailyCalories } from '@/api/diet'
import { getDailyCaloriesBurned } from '@/api/exercise'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

const userStore = useUserStore()
const weightStore = useWeightStore()

// 数据状态
const latestWeight = ref(null)
const todayCalories = ref(860)
const todayBurned = ref(420)
const todaySteps = ref(8542)
const stepsTarget = ref(12000)
const todayWater = ref(1850)
const waterTarget = ref(2500)
const weightChartRef = ref(null)
const trendView = ref('week')

// 今日关注数据
const todayFocus = ref([
  { icon: '🧘', title: '晚间瑜伽', subtitle: '19:30 - 30 分钟', completed: true },
  { icon: '🥗', title: '藜麦沙拉', subtitle: '午餐记录 · 340 kcal', completed: true },
  { icon: '💧', title: '补水提醒', subtitle: '每2小时 · 250ml', completed: false }
])

// 计算属性
const remainingCalories = computed(() => {
  const base = (userStore.user?.targetCalories || 2000) + todayBurned.value - todayCalories.value
  return Math.max(0, base).toLocaleString()
})

const stepsProgress = computed(() => {
  return Math.min(100, Math.round((todaySteps.value / stepsTarget.value) * 100))
})

const waterProgress = computed(() => {
  return Math.min(100, Math.round((todayWater.value / waterTarget.value) * 100))
})

const weightChange = computed(() => -0.4)

const weightChangeClass = computed(() => weightChange.value < 0 ? 'decrease' : 'increase')

// 数据获取
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
    todayCalories.value = caloriesRes.data || 860
    todayBurned.value = burnedRes.data || 420
  } catch (error) {
    console.error('Failed to fetch today stats:', error)
  }
}

const fetchWeightTrend = async () => {
  const days = trendView.value === 'week' ? 7 : 30
  const startDate = dayjs().subtract(days, 'day').format('YYYY-MM-DD')
  const endDate = dayjs().format('YYYY-MM-DD')

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

  // 如果没有数据，使用示例数据
  const chartData = data.length > 0 ? data : generateSampleData()

  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: '#ffffff',
      borderColor: '#bfc9c1',
      borderWidth: 1,
      padding: [12, 16],
      textStyle: {
        color: '#191c1e',
        fontFamily: 'Lexend'
      },
      formatter: (params) => {
        const item = params[0]
        return `<div style="font-weight:600">${item.name}</div><div style="color:#2D6A4F;font-size:16px;margin-top:4px">${item.value} kg</div>`
      }
    },
    grid: {
      left: '0',
      right: '0',
      bottom: '0',
      top: '20px',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: chartData.map(item => dayjs(item.recordDate).format('MM-DD')),
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        color: '#404943',
        fontFamily: 'Lexend',
        fontSize: 12
      }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        color: '#707973',
        fontFamily: 'Lexend',
        fontSize: 11
      },
      splitLine: {
        lineStyle: {
          color: '#e6e8ea',
          type: 'dashed'
        }
      }
    },
    series: [{
      name: '体重',
      type: 'line',
      data: chartData.map(item => item.weight),
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: {
        color: '#2D6A4F',
        width: 3,
        cap: 'round'
      },
      itemStyle: {
        color: '#2D6A4F',
        borderWidth: 3,
        borderColor: '#ffffff'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(45, 106, 79, 0.25)' },
          { offset: 1, color: 'rgba(45, 106, 79, 0.02)' }
        ])
      }
    }]
  }

  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 生成示例数据
const generateSampleData = () => {
  const days = trendView.value === 'week' ? 7 : 30
  const data = []
  for (let i = days - 1; i >= 0; i--) {
    data.push({
      recordDate: dayjs().subtract(i, 'day').format('YYYY-MM-DD'),
      weight: 68 + Math.random() * 0.8 - 0.3
    })
  }
  return data
}

watch(trendView, () => {
  fetchWeightTrend()
})

onMounted(async () => {
  await Promise.all([
    fetchLatestWeight(),
    fetchTodayStats(),
    fetchWeightTrend()
  ])
  await nextTick()
  renderWeightChart()
})
</script>

<style scoped>
/* ==================== 基础变量 ==================== */
.dashboard-container {
  --surface: #f7f9fb;
  --surface-low: #f2f4f6;
  --surface-lowest: #ffffff;
  --on-surface: #191c1e;
  --on-surface-variant: #404943;
  --outline-variant: #bfc9c1;
  --primary: #2D6A4F;
  --primary-light: #95d4b3;
  --secondary: #F97316;
  --tertiary: #0EA5E9;
  --radius-lg: 24px;
  --radius-md: 16px;
  --spacing: 24px;

  padding: var(--spacing);
  min-height: 100vh;
  background: var(--surface);
  font-family: 'Lexend', -apple-system, BlinkMacSystemFont, sans-serif;
}

/* ==================== 页面标题 ==================== */
.page-header {
  margin-bottom: 28px;
}

.page-title {
  font-family: 'Manrope', sans-serif;
  font-size: 32px;
  font-weight: 700;
  color: var(--on-surface);
  margin: 0;
  letter-spacing: -0.02em;
}

/* ==================== Bento 布局 ==================== */
.bento-wrapper {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: var(--spacing);
  align-items: start;
}

.bento-main {
  display: flex;
  flex-direction: column;
  gap: var(--spacing);
}

.bento-sidebar {
  display: flex;
  flex-direction: column;
  gap: var(--spacing);
}

.bento-row {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: var(--spacing);
}

/* ==================== 卡片基础样式 ==================== */
.bento-card {
  background: var(--surface-lowest);
  border-radius: var(--radius-lg);
  padding: 24px;
}

/* 卡片标签 */
.card-label {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.label-icon {
  font-size: 22px;
  line-height: 1;
}

.label-text {
  font-size: 11px;
  font-weight: 600;
  color: var(--on-surface-variant);
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

/* 卡片标题 */
.card-title {
  font-family: 'Manrope', sans-serif;
  font-size: 16px;
  font-weight: 600;
  color: var(--on-surface);
}

.card-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* ==================== 卡路里卡片 ==================== */
.card-calories {
  padding: 28px;
}

.calories-hero {
  margin-bottom: 24px;
}

.hero-number {
  font-family: 'Manrope', sans-serif;
  font-size: 52px;
  font-weight: 800;
  color: var(--primary);
  line-height: 1;
  display: block;
}

.hero-label {
  font-size: 14px;
  color: var(--on-surface-variant);
  margin-top: 4px;
  display: block;
}

.calories-stats {
  display: flex;
  align-items: center;
  gap: 32px;
  padding-top: 20px;
  border-top: 1px solid var(--surface-low);
}

.stat-block {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  font-size: 12px;
  color: #707973;
}

.stat-value {
  font-family: 'Manrope', sans-serif;
  font-size: 20px;
  font-weight: 700;
}

.stat-value.intake {
  color: var(--secondary);
}

.stat-value.burn {
  color: var(--primary);
}

.stat-divider {
  width: 1px;
  height: 36px;
  background: var(--surface-low);
}

/* ==================== 步数卡片 ==================== */
.card-steps {
  display: flex;
  flex-direction: column;
}

.steps-hero {
  margin-bottom: 16px;
}

.steps-hero .hero-number {
  font-size: 36px;
  color: var(--on-surface);
}

.steps-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: auto;
}

.progress-track {
  height: 10px;
  background: var(--surface-low);
  border-radius: 5px;
  overflow: hidden;
}

.progress-track.small {
  height: 8px;
}

.progress-bar {
  height: 100%;
  border-radius: 5px;
  transition: width 0.4s ease;
}

.progress-bar.steps {
  background: linear-gradient(90deg, var(--primary) 0%, var(--primary-light) 100%);
}

.progress-bar.water {
  background: linear-gradient(90deg, var(--tertiary) 0%, #89ceff 100%);
}

.steps-target {
  font-size: 12px;
  color: #707973;
}

/* ==================== 趋势图表卡片 ==================== */
.card-trend {
  padding: 24px 28px;
}

.trend-subtitle {
  font-size: 13px;
  color: var(--on-surface-variant);
  margin: 0 0 20px 0;
}

.view-tabs {
  display: flex;
  gap: 6px;
}

.tab-btn {
  padding: 8px 16px;
  border: none;
  background: var(--surface-low);
  border-radius: 10px;
  font-size: 13px;
  font-family: 'Lexend', sans-serif;
  color: var(--on-surface-variant);
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-btn:hover {
  background: #e6e8ea;
}

.tab-btn.active {
  background: var(--primary);
  color: #ffffff;
}

.chart-area {
  height: 220px;
}

/* ==================== 建议卡片 ==================== */
.card-suggestion {
  background: linear-gradient(145deg, #ecfdf5 0%, #d1fae5 100%);
}

.suggestion-desc {
  font-size: 15px;
  color: var(--on-surface);
  line-height: 1.7;
  margin: 0 0 20px 0;
}

.suggestion-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--primary);
  text-decoration: none;
  transition: gap 0.2s ease;
}

.suggestion-link:hover {
  gap: 12px;
}

.link-arrow {
  font-size: 16px;
}

/* ==================== 今日关注卡片 ==================== */
.card-focus {
  padding: 24px;
}

.focus-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.focus-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px;
  background: var(--surface-low);
  border-radius: var(--radius-md);
}

.focus-icon-wrap {
  font-size: 26px;
  line-height: 1;
}

.focus-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.focus-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--on-surface);
}

.focus-meta {
  font-size: 12px;
  color: #707973;
}

.focus-check {
  font-size: 18px;
  color: #bfc9c1;
}

.focus-check.done {
  color: var(--primary);
}

/* ==================== 体重小卡片 ==================== */
.card-weight-mini {
  padding: 20px 24px;
}

.weight-display {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 8px;
}

.weight-value {
  font-family: 'Manrope', sans-serif;
  font-size: 32px;
  font-weight: 800;
  color: var(--on-surface);
  line-height: 1;
}

.weight-unit {
  font-size: 14px;
  color: var(--on-surface-variant);
}

.weight-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 600;
}

.weight-trend.decrease {
  color: var(--primary);
}

.weight-trend.increase {
  color: var(--secondary);
}

.trend-icon {
  font-size: 14px;
}

/* ==================== 饮水量小卡片 ==================== */
.card-water-mini {
  padding: 20px 24px;
}

.water-display {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 12px;
}

.water-value {
  font-family: 'Manrope', sans-serif;
  font-size: 28px;
  font-weight: 700;
  color: var(--on-surface);
  line-height: 1;
}

.water-unit {
  font-size: 14px;
  color: var(--on-surface-variant);
}

.water-progress {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.water-percent {
  font-size: 12px;
  color: #707973;
}

/* ==================== 响应式 ==================== */
@media (max-width: 1200px) {
  .bento-wrapper {
    grid-template-columns: 1fr;
  }

  .bento-sidebar {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: var(--spacing);
  }

  .card-focus {
    grid-column: span 2;
  }
}

@media (max-width: 900px) {
  .bento-row {
    grid-template-columns: 1fr;
  }

  .bento-sidebar {
    grid-template-columns: 1fr;
  }

  .card-focus {
    grid-column: span 1;
  }
}

@media (max-width: 600px) {
  .dashboard-container {
    padding: 16px;
    --radius-lg: 20px;
    --spacing: 16px;
  }

  .page-title {
    font-size: 26px;
  }

  .hero-number {
    font-size: 42px;
  }
}
</style>
