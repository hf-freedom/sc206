<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>预约管理</span>
          <el-button type="primary" @click="showAddDialog">新建预约</el-button>
        </div>
      </template>

      <el-form :inline="true" style="margin-bottom: 20px">
        <el-form-item label="选择会议室">
          <el-select v-model="selectedRoomId" placeholder="请选择会议室" @change="loadBookings">
            <el-option v-for="room in rooms" :key="room.id" :label="room.name" :value="room.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择日期">
          <el-date-picker v-model="selectedDate" type="date" placeholder="选择日期" @change="loadBookings" />
        </el-form-item>
      </el-form>

      <el-table :data="bookings" style="width: 100%">
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
        <el-table-column label="重要会议" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.important" type="danger">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="签到" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.checkedIn" type="success">已签到</el-tag>
            <el-tag v-else type="info">未签到</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'APPROVED' && !row.checkedIn" type="success" size="small" @click="checkIn(row.id)">
              签到
            </el-button>
            <el-button v-if="row.status !== 'CANCELLED' && row.status !== 'REJECTED' && row.status !== 'NO_SHOW'" type="danger" size="small" @click="cancelBooking(row.id)">
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新建预约" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="会议室">
          <el-select v-model="form.roomId" placeholder="请选择会议室" @change="onRoomChange">
            <el-option v-for="room in rooms" :key="room.id" :label="room.name" :value="room.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="会议主题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="组织者">
          <el-input v-model="form.organizer" />
        </el-form-item>
        <el-form-item label="参会人数">
          <el-input-number v-model="form.attendeeCount" :min="1" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="form.date" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="时间">
          <el-time-picker v-model="form.startTime" format="HH:mm" value-format="HH:mm" />
          <span style="margin: 0 10px">-</span>
          <el-time-picker v-model="form.endTime" format="HH:mm" value-format="HH:mm" />
        </el-form-item>
        <el-form-item label="所需设备">
          <el-select v-model="form.requiredEquipment" multiple placeholder="请选择所需设备">
            <el-option label="投影仪" value="投影仪" />
            <el-option label="白板" value="白板" />
            <el-option label="视频会议" value="视频会议" />
            <el-option label="电视" value="电视" />
            <el-option label="音响系统" value="音响系统" />
          </el-select>
        </el-form-item>
        <el-form-item label="重要会议">
          <el-switch v-model="form.important" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createBooking">提交预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Booking',
  data() {
    return {
      rooms: [],
      bookings: [],
      selectedRoomId: null,
      selectedDate: new Date(),
      dialogVisible: false,
      form: {
        roomId: null,
        title: '',
        organizer: '',
        attendeeCount: 1,
        date: '',
        startTime: '09:00',
        endTime: '10:00',
        requiredEquipment: [],
        important: false
      }
    }
  },
  mounted() {
    this.loadRooms()
  },
  methods: {
    async loadRooms() {
      try {
        const response = await axios.get('http://localhost:8007/api/rooms')
        this.rooms = response.data
        if (this.rooms.length > 0) {
          this.selectedRoomId = this.rooms[0].id
          this.loadBookings()
        }
      } catch (error) {
        this.$message.error('加载会议室列表失败')
      }
    },
    async loadBookings() {
      if (!this.selectedRoomId) return
      try {
        const date = this.formatDate(this.selectedDate)
        const response = await axios.get(`http://localhost:8007/api/bookings/room/${this.selectedRoomId}/date/${date}`)
        this.bookings = response.data
      } catch (error) {
        this.$message.error('加载预约列表失败')
      }
    },
    showAddDialog() {
      this.dialogVisible = true
    },
    onRoomChange() {
      const room = this.rooms.find(r => r.id === this.form.roomId)
      if (room) {
        this.form.startTime = room.openTime
      }
    },
    async createBooking() {
      try {
        await axios.post('http://localhost:8007/api/bookings', this.form)
        this.$message.success('预约成功')
        this.dialogVisible = false
        this.loadBookings()
      } catch (error) {
        this.$message.error(error.response.data || '预约失败')
      }
    },
    async cancelBooking(id) {
      try {
        await axios.put(`http://localhost:8007/api/bookings/${id}/cancel`)
        this.$message.success('取消成功')
        this.loadBookings()
      } catch (error) {
        this.$message.error('取消失败')
      }
    },
    async checkIn(id) {
      try {
        await axios.put(`http://localhost:8007/api/bookings/${id}/checkin`)
        this.$message.success('签到成功')
        this.loadBookings()
      } catch (error) {
        this.$message.error('签到失败')
      }
    },
    getStatusType(status) {
      const map = {
        PENDING_APPROVAL: 'warning',
        APPROVED: 'success',
        CANCELLED: 'info',
        REJECTED: 'danger',
        NO_SHOW: 'danger',
        COMPLETED: 'info'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        PENDING_APPROVAL: '待审批',
        APPROVED: '已通过',
        CANCELLED: '已取消',
        REJECTED: '已拒绝',
        NO_SHOW: '爽约',
        COMPLETED: '已完成'
      }
      return map[status] || status
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
