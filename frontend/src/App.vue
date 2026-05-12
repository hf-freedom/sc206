<template>
  <el-container style="height: 100vh">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu :router="true" default-active="/rooms">
        <el-menu-item index="/rooms">
          <el-icon><OfficeBuilding /></el-icon>
          <span>会议室管理</span>
        </el-menu-item>
        <el-menu-item index="/booking">
          <el-icon><Calendar /></el-icon>
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/approval">
          <el-icon><DocumentChecked /></el-icon>
          <span>审批管理</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><DataLine /></el-icon>
          <span>统计分析</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <div style="float: left; font-size: 20px; font-weight: bold">
          会议室预约系统
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { OfficeBuilding, Calendar, DocumentChecked, DataLine } from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    OfficeBuilding,
    Calendar,
    DocumentChecked,
    DataLine
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
  padding: 0 20px;
}

.el-aside {
  color: #333;
}
</style>
