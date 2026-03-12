import request from './request'

export function getWeightRecords() {
  return request({
    url: '/weight-records',
    method: 'get'
  })
}

export function getWeightTrend(startDate, endDate) {
  return request({
    url: '/weight-records/trend',
    method: 'get',
    params: { startDate, endDate }
  })
}

export function getLatestWeight() {
  return request({
    url: '/weight-records/latest',
    method: 'get'
  })
}

export function createWeightRecord(data) {
  return request({
    url: '/weight-records',
    method: 'post',
    data
  })
}

export function updateWeightRecord(id, data) {
  return request({
    url: `/weight-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteWeightRecord(id) {
  return request({
    url: `/weight-records/${id}`,
    method: 'delete'
  })
}
