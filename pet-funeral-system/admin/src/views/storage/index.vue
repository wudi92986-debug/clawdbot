<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, View } from '@element-plus/icons-vue'
import { getAshStorageList, createAshStorage, renewAshStorage, pickupAshStorage, type AshStorage } from '@/api/ash-storage'
import { getCustomerList } from '@/api/customer'
import { getPetList } from '@/api/pet'

const loading = ref(false)
const searchKeyword = ref('')
const searchStatus = ref<number | ''>('')

const storages = ref<AshStorage[]>([])
const total = ref(0)

const customerOptions = ref<any[]>([])
const petOptions = ref<any[]>([])

const pagination = reactive({
  page: 1,
  pageSize: 10
})

const dialogVisible = ref(false)
const detailVisible = ref(false)
const renewDialogVisible = ref(false)

const formData = reactive<AshStorage>({
  petId: undefined,
  customerId: undefined,
  locationNo: '',
  startDate: '',
  endDate: '',
  fee: 0,
  remark: ''
})

const renewForm = reactive({
  id: 0,
  newEndDate: ''
})

const detailData = ref<AshStorage | null>(null)
const formRef = ref()

const statusMap: Record<number, { text: string; type: string }> = {
  0: { text: '寄存中', type: 'success' },
  1: { text: '已到期', type: 'warning' },
  2: { text: '已取走', type: 'info' }
}

const rules = {
  petId: [{ required: true, message: '请选择宠物', trigger: 'change' }],
  locationNo: [{ required: true, message: '请输入存放位置', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

const fetchStorages = async () => {
  loading.value = true
  try {
    const res = await getAshStorageList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value,
      status: searchStatus.value === '' ? undefined : searchStatus.value
    })
    storages.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchOptions = async () => {
  try {
    const [customerRes] = await Promise.all([
      getCustomerList({ pageSize: 1000 })
    ])
    customerOptions.value = customerRes.data?.records || []
  } catch (error) {
    console.error('获取选项失败:', error)
  }
}

const handleCustomerChange = async (customerId: number) => {
  formData.petId = undefined
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

const handleSearch = () => {
  pagination.page = 1
  fetchStorages()
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleDetail = (row: AshStorage) => {
  detailData.value = row
  detailVisible.value = true
}

const handleRenew = (row: AshStorage) => {
  renewForm.id = row.id!
  renewForm.newEndDate = ''
  renewDialogVisible.value = true
}

const submitRenew = async () => {
  if (!renewForm.newEndDate) {
    ElMessage.warning('请选择新的到期日期')
    return
  }
  try {
    await renewAshStorage(renewForm.id, renewForm.newEndDate)
    ElMessage.success('续期成功')
    renewDialogVisible.value = false
    fetchStorages()
  } catch (error) {
    console.error('续期失败:', error)
  }
}

const handlePickup = async (row: AshStorage) => {
  try {
    await ElMessageBox.confirm('确认登记取走骨灰吗？', '提示', { type: 'warning' })
    await pickupAshStorage(row.id!)
    ElMessage.success('已登记取走')
    fetchStorages()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    await createAshStorage(formData)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    fetchStorages()
  } catch (error: any) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

const resetForm = () => {
  formData.petId = undefined
  formData.customerId = undefined
  formData.locationNo = ''
  formData.startDate = ''
  formData.endDate = ''
  formData.fee = 0
  formData.remark = ''
  petOptions.value = []
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handlePageChange = (page: number) => {
  pagination.page = page
  fetchStorages()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchStorages()
}

onMounted(() => {
  fetchStorages()
  fetchOptions()
})
</script>

<template>
  <div class="storage-container">
    <div class="page-header">
      <h1 class="page-title">骨灰寄存管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增寄存
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索宠物名/客户名"
          clearable
          style="width: 200px"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="searchStatus" placeholder="状态" clearable style="width: 120px; margin-left: 16px">
          <el-option v-for="(item, key) in statusMap" :key="key" :label="item.text" :value="Number(key)" />
        </el-select>
        <el-button type="primary" style="margin-left: 16px" @click="handleSearch">搜索</el-button>
      </div>

      <el-table :data="storages" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="petName" label="宠物名称" min-width="100" />
        <el-table-column prop="customerName" label="客户" min-width="100" />
        <el-table-column prop="locationNo" label="存放位置" min-width="100" />
        <el-table-column prop="startDate" label="开始日期" min-width="120" />
        <el-table-column prop="endDate" label="到期日期" min-width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type as any" effect="light">
              {{ statusMap[row.status]?.text || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-button v-if="row.status === 0" type="warning" link @click="handleRenew(row)">续期</el-button>
            <el-button v-if="row.status === 0" type="success" link @click="handlePickup(row)">取走</el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 新增弹窗 -->
    <el-dialog v-model="dialogVisible" title="新增骨灰寄存" width="500px" @close="handleClose">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="formData.customerId" placeholder="请选择客户" style="width: 100%" @change="handleCustomerChange">
            <el-option v-for="item in customerOptions" :key="item.id" :label="item.name + ' - ' + item.phone" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="宠物" prop="petId">
          <el-select v-model="formData.petId" placeholder="请先选择客户" style="width: 100%" :disabled="!formData.customerId">
            <el-option v-for="item in petOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置" prop="locationNo">
          <el-input v-model="formData.locationNo" placeholder="如：A区-01-03" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="formData.startDate" type="date" placeholder="选择开始日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="到期日期" prop="endDate">
          <el-date-picker v-model="formData.endDate" type="date" placeholder="选择到期日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="费用" prop="fee">
          <el-input-number v-model="formData.fee" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="寄存详情" width="500px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="宠物">{{ detailData.petName }}</el-descriptions-item>
        <el-descriptions-item label="客户">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="存放位置">{{ detailData.locationNo }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="费用">¥{{ detailData.fee }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detailData.status!]?.type as any">{{ statusMap[detailData.status!]?.text }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 续期弹窗 -->
    <el-dialog v-model="renewDialogVisible" title="续期" width="400px">
      <el-form label-width="100px">
        <el-form-item label="新到期日期" required>
          <el-date-picker v-model="renewForm.newEndDate" type="date" placeholder="选择新的到期日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="renewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRenew">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.storage-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; color: var(--text-color-primary); margin: 0; }
.card { background-color: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
.search-bar { display: flex; align-items: center; }
.pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 20px; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
