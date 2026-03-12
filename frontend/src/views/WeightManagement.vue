<template>
  <div class="weight-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>体重管理</span>
          <el-button type="primary" @click="showAddDialog">添加体重记录</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="never">
            <template #header>
              <div class="card-header">
                <span>体重趋势图</span>
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="small"
                  @change="fetchWeightTrend"
                />
              </div>
            </template>
            <div ref="chartRef" style="height: 400px"></div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card shadow="never">
            <template #header>
              <span>最新记录</span>
            </template>
            <div v-if="latestRecord" class="latest-record">
              <div class="record-item">
                <span class="label">体重</span>
                <span class="value">{{ latestRecord.weight }} kg</span>
              </div>
              <el-divider />
              <div class="record-item">
                <span class="label">BMI</span>
                <span class="value">{{ latestRecord.bmi }}</span>
              </div>
              <el-divider />
              <div class="record-item">
                <span class="label">记录日期</span>
                <span class="value">{{ latestRecord.recordDate }}</span>
              </div>
              <el-divider />
              <div v-if="latestRecord.note" class="record-item">
                <span class="label">备注</span>
                <span class="value">{{ latestRecord.note }}</span>
              </div>
            </div>
            <el-empty v-else description="暂无记录" />
          </el-card>
        </el-col>
      </el-row>

      <el-table :data="records" style="margin-top: 20px" stripe>
        <el-table-column prop="recordDate" label="日期" width="120" />
        <el-table-column prop="weight" label="体重 (kg)" width="120" />
        <el-table-column prop="bmi" label="BMI" width="100" />
        <el-table-column prop="note" label="备注" />
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
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="体重 (kg)" prop="weight">
          <el-input-number v-model="form.weight" :min="30" :max="200" :precision="2" />
        </el-form-item>
        <el-form-item label="记录日期" prop="recordDate">
          <el-date-picker
            v-model="form.recordDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
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
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getWeightRecords, getWeightTrend, getLatestWeight, createWeightRecord, updateWeightRecord, deleteWeightRecord } from '@/api/weight'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

const records = ref([])
const latestRecord = ref(null)
const chartRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('添加体重记录')
const formRef = ref(null)
const loading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const dateRange = ref([
  dayjs().subtract(30, 'day').toDate(),
  dayjs().toDate()
])

const form = ref({
  weight: null,
  recordDate: dayjs().format('YYYY-MM-DD'),
  note: ''
})

const rules = {
  weight: [
    { required: true, message: '请输入体重', trigger: 'blur' }
  ],
  recordDate: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ]
}

const fetchRecords = async () => {
  try {
    const res = await getWeightRecords()
    records.value = res.data
  } catch (error) {
    ElMessage.error('获取体重记录失败')
  }
}

const fetchLatest = async () => {
  try {
    const res = await getLatestWeight()
    latestRecord.value = res.data
  } catch (error) {
    console.error('Failed to fetch latest weight:', error)
  }
}

const fetchWeightTrend = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) return

  const startDate = dayjs(dateRange.value[0]).format('YYYY-MM-DD')
  const endDate = dayjs(dateRange.value[1]).format('YYYY-MM-DD')

  try {
    const res = await getWeightTrend(startDate, endDate)
    renderChart(res.data)
  } catch (error) {
    ElMessage.error('获取体重趋势失败')
  }
}

const renderChart = (data) => {
  if (!chartRef.value) return

  const chart = echarts.init(chartRef.value)

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['体重', 'BMI']
    },
    xAxis: {
      type: 'category',
      data: data.map(item => dayjs(item.recordDate).format('MM-DD'))
    },
    yAxis: [
      {
        type: 'value',
        name: '体重 (kg)',
        position: 'left'
      },
      {
        type: 'value',
        name: 'BMI',
        position: 'right'
      }
    ],
    series: [
      {
        name: '体重',
        type: 'line',
        data: data.map(item => item.weight),
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        }
      },
      {
        name: 'BMI',
        type: 'line',
        yAxisIndex: 1,
        data: data.map(item => item.bmi),
        smooth: true,
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  }

  chart.setOption(option)
}

const showAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '添加体重记录'
  form.value = {
    weight: null,
    recordDate: dayjs().format('YYYY-MM-DD'),
    note: ''
  }
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑体重记录'
  form.value = {
    weight: row.weight,
    recordDate: row.recordDate,
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
          await updateWeightRecord(editId.value, form.value)
          ElMessage.success('更新成功')
        } else {
          await createWeightRecord(form.value)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        await fetchRecords()
        await fetchLatest()
        await fetchWeightTrend()
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

    await deleteWeightRecord(id)
    ElMessage.success('删除成功')
    await fetchRecords()
    await fetchLatest()
    await fetchWeightTrend()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(async () => {
  await fetchRecords()
  await fetchLatest()
  await fetchWeightTrend()
  await nextTick()
})
</script>

<style scoped>
.weight-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.latest-record {
  padding: 10px 0;
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.record-item .label {
  color: #909399;
  font-size: 14px;
}

.record-item .value {
  font-weight: bold;
  color: #303133;
  font-size: 16px;
}
</style>
