<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>统计分析</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="loadStatistics"
          />
        </div>
      </template>

      <el-row :gutter="20" style="margin-bottom: 30px">
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align: center">
              <div style="font-size: 40px; color: #409EFF; font-weight: bold">{{ statistics.totalBookings || 0 }}</div>
              <div style="color: #909399; margin-top: 10px">总预约数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align: center">
              <div style="font-size: 40px; color: #67C23A; font-weight: bold">{{ statistics.usageRate ? statistics.usageRate.toFixed(2) : 0 }}%</div>
              <div style="color: #909399; margin-top: 10px">使用率</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align: center">
              <div style="font-size: 40px; color: #E6A23C; font-weight: bold">{{ statistics.cancellationRate ? statistics.cancellationRate.toFixed(2) : 0 }}%</div>
              <div style="color: #909399; margin-top: 10px">取消率</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align: center">
              <div style="font-size: 40px; color: #F56C6C; font-weight: bold">{{ statistics.noShowRate ? statistics.noShowRate.toFixed(2) : 0 }}%</div>
              <div style="color: #909399; margin-top: 10px">爽约率</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>热门时间段</template>
            <el-table :data="popularTimeSlots" style="width: 100%">
              <el-table-column prop="hour" label="时间段" width="150">
                <template #default="{ row }">
                  {{ row.hour }}:00 - {{ row.hour + 1 }}:00
                </template>
              </el-table-column>
              <el-table-column prop="count" label="预约次数" />
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>会议室使用情况</template>
            <el-table :data="roomUsage" style="width: 100%">
              <el-table-column prop="room" label="会议室" />
              <el-table-column prop="count" label="使用次数" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Statistics',
  data() {
    return {
      statistics: {},
      dateRange: [],
      popularTimeSlots: [],
      roomUsage: []
    }
  },
  mounted() {
    this.initDateRange()
    this.loadStatistics()
  },
  methods: {
    initDateRange() {
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 1)
      this.dateRange = [this.formatDate(start), this.formatDate(end)]
    },
    async loadStatistics() {
      if (!this.dateRange || this.dateRange.length < 2) return
      try {
        const response = await axios.get('http://localhost:8007/api/statistics', {
          params: {
            startDate: this.dateRange[0],
            endDate: this.dateRange[1]
          }
        })
        this.statistics = response.data
        
        this.popularTimeSlots = Object.entries(response.data.popularTimeSlots || {}).map(([hour, count]) => ({
          hour: parseInt(hour),
          count
        })).sort((a, b) => b.count - a.count)

        this.roomUsage = Object.entries(response.data.roomUsage || {}).map(([room, count]) => ({
          room,
          count
        })).sort((a, b) => b.count - a.count)
      } catch (error) {
        this.$message.error('加载统计数据失败')
      }
    },
    formatDate(date) {
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>
