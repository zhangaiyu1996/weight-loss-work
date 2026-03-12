import { defineStore } from 'pinia'
import { login, register } from '@/api/auth'
import { getUserProfile } from '@/api/user'
import { setToken, removeToken, setUser, removeUser, getUser } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: getUser(),
    token: localStorage.getItem('token')
  }),

  actions: {
    async login(loginData) {
      const res = await login(loginData)
      this.token = res.data.token
      this.user = res.data.user
      setToken(res.data.token)
      setUser(res.data.user)
      return res
    },

    async register(registerData) {
      const res = await register(registerData)
      return res
    },

    async getUserInfo() {
      const res = await getUserProfile()
      this.user = res.data
      setUser(res.data)
      return res
    },

    logout() {
      this.token = null
      this.user = null
      removeToken()
      removeUser()
    }
  }
})
