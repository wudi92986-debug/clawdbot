<script setup lang="ts">
import { ref, reactive } from 'vue'

const form = reactive({
  companyName: '宠爱天堂宠物殡葬服务中心',
  phone: '400-888-8888',
  address: '北京市朝阳区望京街道xxx号',
  workingHours: '09:00 - 18:00',
  autoConfirm: false,
  notifyCustomer: true,
})

const handleSave = () => { ElMessage.success('保存成功') }
</script>

<template>
  <div class="settings-container">
    <div class="page-header"><h1 class="page-title">基础设置</h1></div>
    <div class="card">
      <el-form :model="form" label-width="120px" style="max-width: 600px">
        <el-form-item label="公司名称"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="公司地址"><el-input v-model="form.address" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="营业时间"><el-input v-model="form.workingHours" /></el-form-item>
        <el-divider />
        <el-form-item label="订单自动确认"><el-switch v-model="form.autoConfirm" /></el-form-item>
        <el-form-item label="通知客户"><el-switch v-model="form.notifyCustomer" /></el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存设置</el-button></el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.settings-container { animation: fadeIn 0.3s ease; }
.page-header { margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
