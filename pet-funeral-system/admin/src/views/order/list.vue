<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, View } from '@element-plus/icons-vue'
import { getOrderList, createOrder, cancelOrder, confirmPayment, completeOrder, type Order } from '@/api/order'
import { getCustomerList } from '@/api/customer'
import { getPetList } from '@/api/pet'
import { getPackageList } from '@/api/package'

const router = useRouter()

// 搜索
const searchKeyword = ref('')
const searchStatus = ref<number | ''>('')
const loading = ref(false)

// 订单列表数据
const orders = ref<Order[]>([])
const total = ref(0)

// 选项数据
const customerOptions = ref<any[]>([])
const petOptions = ref<any[]>([])
const packageOptions = ref<any[]>([])

// 分页参数
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 弹窗控制
const dialogVisible = ref(false)
const payDialogVisible = ref(false)

// 表单数据
const formData = reactive<Order>({
  customerId: 0,
  petId: 0,
  packageId: 0,
  serviceDate: '',
  remark: ''
})

// 支付表单
const payForm = reactive({
  orderId: 0,
  payMethod: 1,
  paidAmount: 0
})

// 表单引用
const formRef = ref()

// 状态映射
const statusMap: Record<number, { text: string; type: string }> = {
  0: { text: '待支付', type: 'warning' },
  1: { text: '已支付', type: 'success' },
  2: { text: '服务中', type: 'primary' },
  3: { text: '已完成', type: 'info' },
  4: { text: '已取消', type: 'danger' }
}

// 支付方式
const payMethods = [
  { value: 1, label: '微信支付' },
  { value: 2, label: '支付宝' },
  { value: 3, label: '银行卡' },
  { value: 4, label: '现金' }
]

// 表单验证规则
const rules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  petId: [{ required: true, message: '请选择宠物', trigger: 'change' }],
  packageId: [{ required: true, message: '请选择服务套餐', trigger: 'change' }],
  serviceDate: [{ required: true, message: '请选择服务日期', trigger: 'change' }]
}

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await getOrderList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value,
      status: searchStatus.value === '' ? undefined : searchStatus.value
    })
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取订单列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取选项数据
const fetchOptions = async () => {
  try {
    const [customerRes, packageRes] = await Promise.all([
      getCustomerList({ pageSize: 1000 }),
      getPackageList({ pageSize: 1000 })
    ])
    customerOptions.value = customerRes.data?.records || []
    packageOptions.value = packageRes.data?.records || []
  } catch (error) {
    console.error('获取选项数据失败:', error)
  }
}

// 客户变化时获取宠物列表
const handleCustomerChange = async (customerId: number) => {
  formData.petId = 0
  if (customerId) {
    try {
      const res = await getPetList({ customerId, pageSize: 100 })
      petOptions.value = res.data?.records || []
    } catch (error) {
      console.error('获取宠物列表失败:', error)
    }
  } else {
    petOptions.value = []
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchOrders()
}

// 新增订单
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 查看详情
const handleDetail = (row: Order) => {
  router.push(`/order/detail/${row.id}`)
}

// 取消订单
const handleCancel = async (row: Order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      type: 'warning'
    })
    await cancelOrder(row.id!)
    ElMessage.success('取消成功')
    fetchOrders()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
    }
  }
}

// 打开支付弹窗
const handlePay = (row: Order) => {
  payForm.orderId = row.id!
  payForm.paidAmount = row.totalAmount || 0
  payForm.payMethod = 1
  payDialogVisible.value = true
}

// 确认支付
const submitPay = async () => {
  try {
    await confirmPayment(payForm.orderId, {
      payMethod: payForm.payMethod,
      paidAmount: payForm.paidAmount
    })
    ElMessage.success('支付确认成功')
    payDialogVisible.value = false
    fetchOrders()
  } catch (error) {
    console.error('支付确认失败:', error)
  }
}

// 完成订单
const handleComplete = async (row: Order) => {
  try {
    await ElMessageBox.confirm('确定要完成该订单吗？', '提示', {
      type: 'info'
    })
    await completeOrder(row.id!)
    ElMessage.success('订单已完成')
    fetchOrders()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    await createOrder(formData)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    fetchOrders()
  } catch (error: any) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

// 重置表单
const resetForm = () => {
  formData.customerId = 0
  formData.petId = 0
  formData.packageId = 0
  formData.serviceDate = ''
  formData.remark = ''
  petOptions.value = []
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page
  fetchOrders()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchOrders()
}

onMounted(() => {
  fetchOrders()
  fetchOptions()
})
</script>

<template>
  <div class="order-container">
    <div class="page-header">
      <h1 class="page-title">订单管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增订单
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索订单号/客户名称"
          clearable
          style="width: 250px"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="searchStatus"
          placeholder="订单状态"
          clearable
          style="width: 150px; margin-left: 16px"
        >
          <el-option
            v-for="(item, key) in statusMap"
            :key="key"
            :label="item.text"
            :value="Number(key)"
          />
        </el-select>
        <el-button type="primary" style="margin-left: 16px" @click="handleSearch">搜索</el-button>
      </div>

      <el-table :data="orders" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="orderNo" label="订单号" min-width="150" />
        <el-table-column prop="customerName" label="客户" min-width="100" />
        <el-table-column prop="petName" label="宠物" min-width="100" />
        <el-table-column prop="packageName" label="套餐" min-width="120" />
        <el-table-column prop="totalAmount" label="金额" min-width="100">
          <template #default="{ row }">
            <span style="color: #E8B89D; font-weight: 500">¥{{ row.totalAmount?.toLocaleString() || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type as any" effect="light">
              {{ statusMap[row.status]?.text || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serviceDate" label="服务日期" min-width="120" />
        <el-table-column prop="createdAt" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              type="success" 
              link 
              @click="handlePay(row)"
            >
              确认支付
            </el-button>
            <el-button 
              v-if="row.status === 1 || row.status === 2" 
              type="primary" 
              link 
              @click="handleComplete(row)"
            >
              完成
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              type="danger" 
              link 
              @click="handleCancel(row)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 新增订单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增订单"
      width="500px"
      @close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="客户" prop="customerId">
          <el-select 
            v-model="formData.customerId" 
            placeholder="请选择客户" 
            style="width: 100%"
            @change="handleCustomerChange"
          >
            <el-option
              v-for="item in customerOptions"
              :key="item.id"
              :label="item.name + ' - ' + item.phone"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="宠物" prop="petId">
          <el-select 
            v-model="formData.petId" 
            placeholder="请先选择客户" 
            style="width: 100%"
            :disabled="!formData.customerId"
          >
            <el-option
              v-for="item in petOptions"
              :key="item.id"
              :label="item.name + ' (' + item.species + ')'"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务套餐" prop="packageId">
          <el-select v-model="formData.packageId" placeholder="请选择服务套餐" style="width: 100%">
            <el-option
              v-for="item in packageOptions"
              :key="item.id"
              :label="item.name + ' - ¥' + item.price"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务日期" prop="serviceDate">
          <el-date-picker
            v-model="formData.serviceDate"
            type="date"
            placeholder="选择服务日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 支付确认弹窗 -->
    <el-dialog
      v-model="payDialogVisible"
      title="确认支付"
      width="400px"
    >
      <el-form label-width="80px">
        <el-form-item label="支付金额">
          <el-input-number v-model="payForm.paidAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="payForm.payMethod" style="width: 100%">
            <el-option
              v-for="item in payMethods"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPay">确认支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.order-container {
  animation: fadeIn 0.3s ease;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color-primary);
  margin: 0;
}

.card {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
}

.search-bar {
  display: flex;
  align-items: center;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
