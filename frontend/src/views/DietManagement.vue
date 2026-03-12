<template>
  <div class="diet-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>饮食管理</span>
          <el-button type="primary" @click="showAddDialog">添加饮食记录</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="never">
            <template #header>
              <div class="card-header">
                <span>每日卡路里统计</span>
                <el-date-picker
                  v-model="selectedDate"
                  type="date"
                  placeholder="选择日期"
                  @change="fetchRecordsByDate"
                  style="width: 200px"
                />
              </div>
            </template>
            <div ref="calorieChartRef" style="height: 300px"></div>
          </el-card>

          <el-card shadow="never" style="margin-top: 20px">
            <template #header>
              <span>营养成分分析</span>
            </template>
            <div ref="nutritionChartRef" style="height: 300px"></div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card shadow="never">
            <template #header>
              <span>今日统计</span>
            </template>
            <div class="today-stats">
              <div class="stat-item">
                <span class="label">总卡路里</span>
                <span class="value">{{ todayTotal.calories }} kcal</span>
              </div>
              <el-divider />
              <div class="stat-item">
                <span class="label">蛋白质</span>
                <span class="value">{{ todayTotal.protein }} g</span>
              </div>
              <el-divider />
              <div class="stat-item">
                <span class="label">碳水化合物</span>
                <span class="value">{{ todayTotal.carbohydrate }} g</span>
              </div>
              <el-divider />
              <div class="stat-item">
                <span class="label">脂肪</span>
                <span class="value">{{ todayTotal.fat }} g</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="table-container">
        <el-table :data="records" stripe height="480">
          <el-table-column prop="recordDate" label="日期" width="120" />
          <el-table-column prop="recordTime" label="时间" width="100" />
          <el-table-column prop="mealType" label="餐次" width="100" />
          <el-table-column prop="foodName" label="食物" />
          <el-table-column prop="calories" label="卡路里" width="100" />
          <el-table-column prop="portion" label="份量" width="100" />
          <el-table-column label="操作" width="180">
            <template #default="{ row }">
              <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页功能预留位置 -->
        <!-- <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="margin-top: 20px; justify-content: flex-end"
        /> -->
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="餐次" prop="mealType">
          <el-select v-model="form.mealType" placeholder="请选择餐次">
            <el-option label="早餐" value="早餐" />
            <el-option label="午餐" value="午餐" />
            <el-option label="晚餐" value="晚餐" />
            <el-option label="加餐" value="加餐" />
          </el-select>
        </el-form-item>
        <el-form-item label="食物名称" prop="foodName">
          <el-input v-model="form.foodName" placeholder="请输入食物名称" />
        </el-form-item>
        <el-form-item label="卡路里 (kcal)" prop="calories">
          <el-input-number v-model="form.calories" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="蛋白质 (g)" prop="protein">
          <el-input-number v-model="form.protein" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="碳水化合物 (g)" prop="carbohydrate">
          <el-input-number v-model="form.carbohydrate" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="脂肪 (g)" prop="fat">
          <el-input-number v-model="form.fat" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="份量" prop="portion">
          <el-input v-model="form.portion" placeholder="例如：1碗、100g" />
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
import { getDietRecords, getDietRecordsByDate, createDietRecord, updateDietRecord, deleteDietRecord } from '@/api/diet'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

const records = ref([])
const selectedDate = ref(new Date())
const calorieChartRef = ref(null)
const nutritionChartRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('添加饮食记录')
const formRef = ref(null)
const loading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const form = ref({
  mealType: '',
  foodName: '',
  calories: null,
  protein: null,
  carbohydrate: null,
  fat: null,
  portion: '',
  recordDate: dayjs().format('YYYY-MM-DD'),
  recordTime: dayjs().format('HH:mm:ss'),
  note: ''
})

const rules = {
  mealType: [
    { required: true, message: '请选择餐次', trigger: 'change' }
  ],
  foodName: [
    { required: true, message: '请输入食物名称', trigger: 'blur' }
  ],
  recordDate: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ]
}

const todayTotal = computed(() => {
  const today = dayjs(selectedDate.value).format('YYYY-MM-DD')
  const todayRecords = records.value.filter(r => r.recordDate === today)

  return {
    calories: todayRecords.reduce((sum, r) => sum + (r.calories || 0), 0).toFixed(2),
    protein: todayRecords.reduce((sum, r) => sum + (r.protein || 0), 0).toFixed(2),
    carbohydrate: todayRecords.reduce((sum, r) => sum + (r.carbohydrate || 0), 0).toFixed(2),
    fat: todayRecords.reduce((sum, r) => sum + (r.fat || 0), 0).toFixed(2)
  }
})

const fetchRecords = async () => {
  try {
    const res = await getDietRecords()
    records.value = res.data
    renderCharts()
  } catch (error) {
    ElMessage.error('获取饮食记录失败')
  }
}

const fetchRecordsByDate = async () => {
  await fetchRecords()
}

const renderCharts = () => {
  renderCalorieChart()
  renderNutritionChart()
}

const renderCalorieChart = () => {
  if (!calorieChartRef.value) return

  const chart = echarts.init(calorieChartRef.value)
  const today = dayjs(selectedDate.value).format('YYYY-MM-DD')
  const todayRecords = records.value.filter(r => r.recordDate === today)

  const mealTypes = ['早餐', '午餐', '晚餐', '加餐']
  const data = mealTypes.map(type => {
    const mealRecords = todayRecords.filter(r => r.mealType === type)
    return mealRecords.reduce((sum, r) => sum + (r.calories || 0), 0)
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
      data: mealTypes
    },
    yAxis: {
      type: 'value',
      name: '卡路里 (kcal)'
    },
    series: [
      {
        name: '卡路里',
        type: 'bar',
        data: data,
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  }

  chart.setOption(option)
}

const renderNutritionChart = () => {
  if (!nutritionChartRef.value) return

  const chart = echarts.init(nutritionChartRef.value)
  const today = dayjs(selectedDate.value).format('YYYY-MM-DD')
  const todayRecords = records.value.filter(r => r.recordDate === today)

  const protein = todayRecords.reduce((sum, r) => sum + (r.protein || 0), 0)
  const carbohydrate = todayRecords.reduce((sum, r) => sum + (r.carbohydrate || 0), 0)
  const fat = todayRecords.reduce((sum, r) => sum + (r.fat || 0), 0)

  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '营养成分',
        type: 'pie',
        radius: '50%',
        data: [
          { value: protein, name: '蛋白质' },
          { value: carbohydrate, name: '碳水化合物' },
          { value: fat, name: '脂肪' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  chart.setOption(option)
}

const showAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '添加饮食记录'
  form.value = {
    mealType: '',
    foodName: '',
    calories: null,
    protein: null,
    carbohydrate: null,
    fat: null,
    portion: '',
    recordDate: dayjs().format('YYYY-MM-DD'),
    recordTime: dayjs().format('HH:mm:ss'),
    note: ''
  }
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑饮食记录'
  form.value = {
    mealType: row.mealType,
    foodName: row.foodName,
    calories: row.calories,
    protein: row.protein,
    carbohydrate: row.carbohydrate,
    fat: row.fat,
    portion: row.portion,
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
          await updateDietRecord(editId.value, form.value)
          ElMessage.success('更新成功')
        } else {
          await createDietRecord(form.value)
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

    await deleteDietRecord(id)
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
  renderCharts()
})
</script>

<style scoped>
.diet-management {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
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

.table-container {
  margin-top: 20px;
}
</style>
