<template>
  <div class="exercise-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>运动管理</span>
          <el-button type="primary" @click="showAddDialog">添加运动记录</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="never">
            <template #header>
              <div class="card-header">
                <span>每日消耗统计</span>
                <el-date-picker
                  v-model="selectedDate"
                  type="date"
                  placeholder="选择日期"
                  size="small"
                  @change="fetchRecordsByDate"
                />
              </div>
            </template>
            <div ref="chartRef" style="height: 400px"></div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card shadow="never">
            <template #header>
              <span>今日统计</span>
            </template>
            <div class="today-stats">
              <div class="stat-item">
                <span class="label">总消耗</span>
                <span class="value">{{ todayTotal.calories }} kcal</span>
              </div>
              <el-divider />
              <div class="stat-item">
                <span class="label">运动时长</span>
                <span class="value">{{ todayTotal.duration }} 分钟</span>
              </div>
              <el-divider />
              <div class="stat-item">
                <span class="label">运动次数</span>
                <span class="value">{{ todayTotal.count }} 次</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-table :data="records" style="margin-top: 20px" stripe>
        <el-table-column prop="recordDate" label="日期" width="120" />
        <el-table-column prop="recordTime" label="时间" width="100" />
        <el-table-column prop="exerciseType" label="类型" width="120" />
        <el-table-column prop="exerciseName" label="运动名称" />
        <el-table-column prop="duration" label="时长(分钟)" width="120" />
        <el-table-column prop="caloriesBurned" label="消耗(kcal)" width="120" />
        <el-table-column prop="intensity" label="强度" width="100" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="运动类型" prop="exerciseType">
          <el-select v-model="form.exerciseType" placeholder="请选择运动类型">
            <el-option label="有氧运动" value="有氧运动" />
            <el-option label="力量训练" value="力量训练" />
            <el-option label="瑜伽" value="瑜伽" />
            <el-option label="球类运动" value="球类运动" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="运动名称" prop="exerciseName">
          <el-input v-model="form.exerciseName" placeholder="例如：跑步、游泳" />
        </el-form-item>
        <el-form-item label="时长 (分钟)" prop="duration">
          <el-input-number v-model="form.duration" :min="0" />
        </el-form-item>
        <el-form-item label="消耗 (kcal)" prop="caloriesBurned">
          <el-input-number v-model="form.caloriesBurned" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="距离 (km)" prop="distance">
          <el-input-number v-model="form.distance" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="强度" prop="intensity">
          <el-select v-model="form.intensity" placeholder="请选择强度">
            <el-option label="低" value="低" />
            <el-option label="中" value="中" />
            <el-option label="高" value="高" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录日期" prop="recordDate">
          <el-date-picker
            v-model="form.recordDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="记录时间" prop="recordTime">
          <el-time-picker
            v-model="form.recordTime"
            placeholder="选择时间"
            value-format="HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExerciseRecords, createExerciseRecord, updateExerciseRecord, deleteExerciseRecord } from '@/api/exercise'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

const records = ref([])
const selectedDate = ref(new Date())
const chartRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('添加运动记录')
const formRef = ref(null)
const loading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const form = ref({
  exerciseType: '',
  exerciseName: '',
  duration: null,
  caloriesBurned: null,
  distance: null,
  intensity: '',
  recordDate: dayjs().format('YYYY-MM-DD'),
  recordTime: dayjs().format('HH:mm:ss'),
  note: ''
})

const rules = {
  exerciseType: [
    { required: true, message: '请选择运动类型', trigger: 'change' }
  ],
  exerciseName: [
    { required: true, message: '请输入运动名称', trigger: 'blur' }
  ],
  recordDate: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ]
}

const todayTotal = computed(() => {
  const today = dayjs(selectedDate.value).format('YYYY-MM-DD')
  const todayRecords = records.value.filter(r => r.recordDate === today)

  return {
    calories: todayRecords.reduce((sum, r) => sum + (r.caloriesBurned || 0), 0).toFixed(2),
    duration: todayRecords.reduce((sum, r) => sum + (r.duration || 0), 0),
    count: todayRecords.length
  }
})

const fetchRecords = async () => {
  try {
    const res = await getExerciseRecords()
    records.value = res.data
    renderChart()
  } catch (error) {
    ElMessage.error('获取运动记录失败')
  }
}

const fetchRecordsByDate = async () => {
  await fetchRecords()
}

const renderChart = () => {
  if (!chartRef.value) return

  const chart = echarts.init(chartRef.value)
  const today = dayjs(selectedDate.value).format('YYYY-MM-DD')
  const todayRecords = records.value.filter(r => r.recordDate === today)

  const exerciseNames = [...new Set(todayRecords.map(r => r.exerciseName))]
  const data = exerciseNames.map(name => {
    const exerciseRecords = todayRecords.filter(r => r.exerciseName === name)
    return exerciseRecords.reduce((sum, r) => sum + (r.caloriesBurned || 0), 0)
  })

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: exerciseNames,
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      name: '消耗 (kcal)'
    },
    series: [
      {
        name: '卡路里消耗',
        type: 'bar',
        data: data,
        itemStyle: {
          color: '#E6A23C'
        }
      }
    ]
  }

  chart.setOption(option)
}

const showAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '添加运动记录'
  form.value = {
    exerciseType: '',
    exerciseName: '',
    duration: null,
    caloriesBurned: null,
    distance: null,
    intensity: '',
    recordDate: dayjs().format('YYYY-MM-DD'),
    recordTime: dayjs().format('HH:mm:ss'),
    note: ''
  }
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑运动记录'
  form.value = {
    exerciseType: row.exerciseType,
    exerciseName: row.exerciseName,
    duration: row.duration,
    caloriesBurned: row.caloriesBurned,
    distance: row.distance,
    intensity: row.intensity,
    recordDate: row.recordDate,
    recordTime: row.recordTime,
    note: row.note
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isEdit.value) {
          await updateExerciseRecord(editId.value, form.value)
          ElMessage.success('更新成功')
        } else {
          await createExerciseRecord(form.value)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        await fetchRecords()
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '操作失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteExerciseRecord(id)
    ElMessage.success('删除成功')
    await fetchRecords()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(async () => {
  await fetchRecords()
  await nextTick()
  renderChart()
})
</script>

<style scoped>
.exercise-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.today-stats {
  padding: 10px 0;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.stat-item .label {
  color: #909399;
  font-size: 14px;
}

.stat-item .value {
  font-weight: bold;
  color: #303133;
  font-size: 16px;
}
</style>
