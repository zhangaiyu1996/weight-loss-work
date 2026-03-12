import { defineStore } from 'pinia'
import { getWeightRecords, getWeightTrend, getLatestWeight } from '@/api/weight'

export const useWeightStore = defineStore('weight', {
  state: () => ({
    records: [],
    latestRecord: null,
    trendData: []
  }),

  actions: {
    async fetchRecords() {
      const res = await getWeightRecords()
      this.records = res.data
      return res
    },

    async fetchTrend(startDate, endDate) {
      const res = await getWeightTrend(startDate, endDate)
      this.trendData = res.data
      return res
    },

    async fetchLatest() {
      const res = await getLatestWeight()
      this.latestRecord = res.data
      return res
    }
  }
})
