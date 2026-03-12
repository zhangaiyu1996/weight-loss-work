import request from './request'

export function getUserProfile() {
  return request({
    url: '/users/profile',
    method: 'get'
  })
}

export function updateUserProfile(data) {
  return request({
    url: '/users/profile',
    method: 'put',
    data
  })
}
