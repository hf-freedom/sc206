<template>
  <div>
    <el-card>
      <template #header>
        <span>审批管理</span>
      </template>
      <el-table :data="pendingBookings" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roomName" label="会议室" width="120" />
        <el-table-column prop="title" label="会议主题" width="150" />
        <el-table-column prop="organizer" label="组织者" width="100" />
        <el-table-column label="时间" width="200">
          <template #default="{ row }">
            {{ row.date }} {{ row.startTime }} - {{ row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="attendeeCount" label="参会人数" width="100" />
        <el-table-column label="所需设备" width="200">
          <template #default="{ row }">
            <el-tag v-for="item in row.requiredEquipment" :key="item" style="margin-right: 5px">
              {{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="申请时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="approve(row.id)">
              通过
            </el-button>
            <el-button type="danger" size="small" @click="reject(row.id)">
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Approval',
  data() {
    return {
      pendingBookings: []
    }
  },
  mounted() {
    this.loadPendingBookings()
  },
  methods: {
    async loadPendingBookings() {
      try {
        const response = await axios.get('http://localhost:8007/api/bookings/pending')
        this.pendingBookings = response.data
      } catch (error) {
        this.$message.error('加载待审批列表失败')
      }
    },
    async approve(id) {
      try {
        await axios.put(`http://localhost:8007/api/bookings/${id}/approve`)
        this.$message.success('审批通过')
        this.loadPendingBookings()
      } catch (error) {
        this.$message.error('操作失败')
      }
    },
    async reject(id) {
      try {
        await axios.put(`http://localhost:8007/api/bookings/${id}/reject`)
        this.$message.success('已拒绝')
        this.loadPendingBookings()
      } catch (error) {
        this.$message.error('操作失败')
      }
    }
  }
}
</script>
