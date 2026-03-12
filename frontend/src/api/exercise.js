import request from './request'

export function getExerciseRecords() {
  return request({
    url: '/exercise-records',
    method: 'get'
  })
}

export function getExerciseRecordsByDate(date) {
  return request({
    url: `/exercise-records/date/${date}`,
    method: 'get'
  })
}

export function getDailyCaloriesBurned(date) {
  return request({
    url: '/exercise-records/statistics',
    method: 'get',
    params: { date }
  })
}

export function createExerciseRecord(data) {
  return request({
    url: '/exercise-records',
    method: 'post',
    data
  })
}

export function updateExerciseRecord(id, data) {
  return request({
    url: `/exercise-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteExerciseRecord(id) {
  return request({
    url: `/exercise-records/${id}`,
    method: 'delete'
  })
}
