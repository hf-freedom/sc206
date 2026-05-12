<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>会议室列表</span>
          <el-button type="primary" @click="showAddDialog">添加会议室</el-button>
        </div>
      </template>
      <el-table :data="rooms" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="会议室名称" width="150" />
        <el-table-column prop="floor" label="楼层" width="80" />
        <el-table-column prop="capacity" label="容量" width="80" />
        <el-table-column prop="equipment" label="设备">
          <template #default="{ row }">
            <el-tag v-for="item in row.equipment" :key="item" style="margin-right: 5px">
              {{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开放时间" width="150">
          <template #default="{ row }">
            {{ row.openTime }} - {{ row.closeTime }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.available ? 'success' : 'danger'">
              {{ row.available ? '可用' : '不可用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="添加会议室" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="会议室名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="楼层">
          <el-input-number v-model="form.floor" :min="1" />
        </el-form-item>
        <el-form-item label="容量">
          <el-input-number v-model="form.capacity" :min="1" />
        </el-form-item>
        <el-form-item label="设备">
          <el-select v-model="form.equipment" multiple placeholder="请选择设备">
            <el-option label="投影仪" value="投影仪" />
            <el-option label="白板" value="白板" />
            <el-option label="视频会议" value="视频会议" />
            <el-option label="电视" value="电视" />
            <el-option label="音响系统" value="音响系统" />
          </el-select>
        </el-form-item>
        <el-form-item label="开放时间">
          <el-time-picker v-model="form.openTime" format="HH:mm" value-format="HH:mm" />
          <span style="margin: 0 10px">-</span>
          <el-time-picker v-model="form.closeTime" format="HH:mm" value-format="HH:mm" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addRoom">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Rooms',
  data() {
    return {
      rooms: [],
      dialogVisible: false,
      form: {
        name: '',
        floor: 1,
        capacity: 10,
        equipment: [],
        openTime: '08:00',
        closeTime: '20:00'
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
      } catch (error) {
        this.$message.error('加载会议室列表失败')
      }
    },
    showAddDialog() {
      this.dialogVisible = true
    },
    async addRoom() {
      try {
        await axios.post('http://localhost:8007/api/rooms', this.form)
        this.$message.success('添加成功')
        this.dialogVisible = false
        this.loadRooms()
      } catch (error) {
        this.$message.error('添加失败')
      }
    }
  }
}
</script>
