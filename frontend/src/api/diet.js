import request from './request'

export function getDietRecords() {
  return request({
    url: '/diet-records',
    method: 'get'
  })
}

export function getDietRecordsByDate(date) {
  return request({
    url: `/diet-records/date/${date}`,
    method: 'get'
  })
}

export function getDailyCalories(date) {
  return request({
    url: '/diet-records/statistics',
    method: 'get',
    params: { date }
  })
}

export function createDietRecord(data) {
  return request({
    url: '/diet-records',
    method: 'post',
    data
  })
}

export function updateDietRecord(id, data) {
  return request({
    url: `/diet-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteDietRecord(id) {
  return request({
    url: `/diet-records/${id}`,
    method: 'delete'
  })
}
