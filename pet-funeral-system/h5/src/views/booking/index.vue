<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 表单数据
const form = reactive({
  packageId: route.query.package || '2',
  petName: '',
  petType: 'dog',
  petWeight: '',
  appointmentDate: '',
  appointmentTime: '',
  address: '',
  contactName: '',
  contactPhone: '',
  remark: '',
})

// 套餐选项
const packages = [
  { id: '1', name: '基础告别', price: 1280 },
  { id: '2', name: '温馨告别', price: 2980 },
  { id: '3', name: '尊享告别', price: 5980 },
]

// 宠物类型
const petTypes = [
  { value: 'dog', label: '🐕 狗' },
  { value: 'cat', label: '🐱 猫' },
  { value: 'other', label: '🐰 其他' },
]

// 体重选项
const weightOptions = [
  { value: '1', label: '5kg以下' },
  { value: '2', label: '5-15kg' },
  { value: '3', label: '15-30kg' },
  { value: '4', label: '30kg以上' },
]

// 时间选项
const timeOptions = ['09:00', '10:00', '11:00', '14:00', '15:00', '16:00', '17:00', '18:00']

// 弹窗控制
const showDatePicker = ref(false)
const showTimePicker = ref(false)

// 当前套餐
const currentPackage = computed(() => packages.find(p => p.id === form.packageId) || packages[1])

// 日期确认
const onDateConfirm = ({ selectedValues }: any) => {
  form.appointmentDate = selectedValues.join('-')
  showDatePicker.value = false
}

// 提交
const handleSubmit = () => {
  if (!form.petName) {
    showToast('请输入宠物名字')
    return
  }
  if (!form.appointmentDate) {
    showToast('请选择预约日期')
    return
  }
  if (!form.appointmentTime) {
    showToast('请选择预约时间')
    return
  }
  if (!form.address) {
    showToast('请输入接运地址')
    return
  }
  if (!form.contactPhone) {
    showToast('请输入联系电话')
    return
  }

  // 提交预约
  showConfirmDialog({
    title: '确认预约',
    message: `套餐：${currentPackage.value.name}\n费用：¥${currentPackage.value.price}`,
  }).then(() => {
    showToast('预约成功')
    router.push('/order')
  })
}

// 返回
const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="booking-page">
    <van-nav-bar title="预约服务" left-arrow @click-left="goBack" />

    <van-form @submit="handleSubmit">
      <!-- 选择套餐 -->
      <van-cell-group inset title="选择服务套餐">
        <van-radio-group v-model="form.packageId">
          <van-cell
            v-for="pkg in packages"
            :key="pkg.id"
            :title="pkg.name"
            :label="'¥' + pkg.price + '起'"
            clickable
            @click="form.packageId = pkg.id"
          >
            <template #right-icon>
              <van-radio :name="pkg.id" />
            </template>
          </van-cell>
        </van-radio-group>
      </van-cell-group>

      <!-- 宠物信息 -->
      <van-cell-group inset title="宠物信息">
        <van-field
          v-model="form.petName"
          label="宠物名字"
          placeholder="请输入宠物名字"
          required
        />
        <van-field label="宠物类型" required>
          <template #input>
            <van-radio-group v-model="form.petType" direction="horizontal">
              <van-radio v-for="type in petTypes" :key="type.value" :name="type.value">
                {{ type.label }}
              </van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field label="宠物体重" required>
          <template #input>
            <van-radio-group v-model="form.petWeight" direction="horizontal">
              <van-radio v-for="w in weightOptions" :key="w.value" :name="w.value">
                {{ w.label }}
              </van-radio>
            </van-radio-group>
          </template>
        </van-field>
      </van-cell-group>

      <!-- 预约信息 -->
      <van-cell-group inset title="预约信息">
        <van-field
          v-model="form.appointmentDate"
          label="预约日期"
          placeholder="请选择日期"
          readonly
          required
          is-link
          @click="showDatePicker = true"
        />
        <van-field label="预约时间" required>
          <template #input>
            <div class="time-grid">
              <div
                v-for="time in timeOptions"
                :key="time"
                class="time-item"
                :class="{ active: form.appointmentTime === time }"
                @click="form.appointmentTime = time"
              >
                {{ time }}
              </div>
            </div>
          </template>
        </van-field>
        <van-field
          v-model="form.address"
          label="接运地址"
          placeholder="请输入详细地址"
          type="textarea"
          rows="2"
          required
        />
        <van-field
          v-model="form.contactName"
          label="联系人"
          placeholder="请输入联系人姓名"
        />
        <van-field
          v-model="form.contactPhone"
          label="联系电话"
          placeholder="请输入手机号"
          type="tel"
          required
        />
        <van-field
          v-model="form.remark"
          label="备注"
          placeholder="如有特殊要求请备注"
          type="textarea"
          rows="2"
        />
      </van-cell-group>

      <!-- 费用 -->
      <div class="price-bar">
        <div class="price-info">
          <span class="label">套餐费用</span>
          <span class="price">¥{{ currentPackage.price.toLocaleString() }}</span>
        </div>
      </div>

      <!-- 提交按钮 -->
      <div class="submit-bar">
        <van-button type="primary" round block native-type="submit">
          提交预约
        </van-button>
      </div>
    </van-form>

    <!-- 日期选择器 -->
    <van-popup v-model:show="showDatePicker" position="bottom" round>
      <van-date-picker
        :min-date="new Date()"
        @confirm="onDateConfirm"
        @cancel="showDatePicker = false"
      />
    </van-popup>
  </div>
</template>

<script lang="ts">
import { computed } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
export default {
  setup() {
    return { showToast, showConfirmDialog, computed }
  }
}
</script>

<style lang="scss" scoped>
.booking-page {
  min-height: 100vh;
  background-color: var(--bg-color);
  padding-bottom: 100px;
}

.time-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
  width: 100%;

  .time-item {
    padding: 8px;
    text-align: center;
    border-radius: 6px;
    background-color: var(--bg-color);
    font-size: 13px;
    cursor: pointer;

    &.active {
      background-color: var(--color-primary);
      color: #fff;
    }
  }
}

.price-bar {
  margin: 16px;
  padding: 16px;
  background-color: #fff;
  border-radius: 12px;

  .price-info {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .label {
      color: var(--text-color);
    }

    .price {
      font-size: 24px;
      font-weight: 700;
      color: var(--color-warning);
    }
  }
}

.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  padding-bottom: calc(12px + env(safe-area-inset-bottom));
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}
</style>
