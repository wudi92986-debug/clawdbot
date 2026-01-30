<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, View } from '@element-plus/icons-vue'
import { getCeremonyList, createCeremony, updateCeremony, startCeremony, completeCeremony, cancelCeremony, type Ceremony } from '@/api/ceremony'
import { getOrderList } from '@/api/order'

const loading = ref(false)
const searchStatus = ref<number | ''>('')

const ceremonies = ref<Ceremony[]>([])
const total = ref(0)
const orderOptions = ref<any[]>([])

const pagination = reactive({
  page: 1,
  pageSize: 10
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增仪式')
const detailVisible = ref(false)

const formData = reactive<Ceremony>({
  orderId: undefined,
  ceremonyType: 0,
  hallNo: '',
  startTime: '',
  endTime: '',
  contactName: '',
  contactPhone: '',
  attendeeCount: 0,
  specialRequests: '',
  remark: ''
})

const detailData = ref<Ceremony | null>(null)
const formRef = ref()

const statusMap: Record<number, { text: string; type: string }> = {
  0: { text: '待开始', type: 'warning' },
  1: { text: '进行中', type: 'primary' },
  2: { text: '已完成', type: 'success' },
  3: { text: '已取消', type: 'info' }
}

const ceremonyTypes = ['简约告别', '标准告别', '豪华告别', '定制告别']
const hallOptions = ['A厅', 'B厅', 'C厅', 'VIP厅']

const rules = {
  orderId: [{ required: true, message: '请选择关联订单', trigger: 'change' }],
  hallNo: [{ required: true, message: '请选择仪式厅', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }]
}

const fetchCeremonies = async () => {
  loading.value = true
  try {
    const res = await getCeremonyList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      status: searchStatus.value === '' ? undefined : searchStatus.value
    })
    ceremonies.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchOrders = async () => {
  try {
    const res = await getOrderList({ pageSize: 1000 })
    orderOptions.value = res.data?.records || []
  } catch (error) {
    console.error('获取订单失败:', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchCeremonies()
}

const handleAdd = () => {
  dialogTitle.value = '新增仪式'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: Ceremony) => {
  dialogTitle.value = '编辑仪式'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDetail = (row: Ceremony) => {
  detailData.value = row
  detailVisible.value = true
}

const handleStart = async (row: Ceremony) => {
  try {
    await ElMessageBox.confirm('确认开始仪式吗？', '提示', { type: 'info' })
    await startCeremony(row.id!)
    ElMessage.success('仪式已开始')
    fetchCeremonies()
  } catch (error: any) {
    if (error !== 'cancel') console.error('操作失败:', error)
  }
}

const handleComplete = async (row: Ceremony) => {
  try {
    await ElMessageBox.confirm('确认完成仪式吗？', '提示', { type: 'info' })
    await completeCeremony(row.id!)
    ElMessage.success('仪式已完成')
    fetchCeremonies()
  } catch (error: any) {
    if (error !== 'cancel') console.error('操作失败:', error)
  }
}

const handleCancel = async (row: Ceremony) => {
  try {
    await ElMessageBox.confirm('确认取消仪式吗？', '提示', { type: 'warning' })
    await cancelCeremony(row.id!)
    ElMessage.success('仪式已取消')
    fetchCeremonies()
  } catch (error: any) {
    if (error !== 'cancel') console.error('操作失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    if (formData.id) {
      await updateCeremony(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createCeremony(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchCeremonies()
  } catch (error: any) {
    if (error !== false) console.error('提交失败:', error)
  }
}

const resetForm = () => {
  formData.id = undefined
  formData.orderId = undefined
  formData.ceremonyType = 0
  formData.hallNo = ''
  formData.startTime = ''
  formData.endTime = ''
  formData.contactName = ''
  formData.contactPhone = ''
  formData.attendeeCount = 0
  formData.specialRequests = ''
  formData.remark = ''
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handlePageChange = (page: number) => {
  pagination.page = page
  fetchCeremonies()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchCeremonies()
}

onMounted(() => {
  fetchCeremonies()
  fetchOrders()
})
</script>

<template>
  <div class="ceremony-container">
    <div class="page-header">
      <h1 class="page-title">告别仪式管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增仪式
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-select v-model="searchStatus" placeholder="状态筛选" clearable style="width: 150px">
          <el-option v-for="(item, key) in statusMap" :key="key" :label="item.text" :value="Number(key)" />
        </el-select>
        <el-button type="primary" style="margin-left: 16px" @click="handleSearch">搜索</el-button>
      </div>

      <el-table :data="ceremonies" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="petName" label="宠物" min-width="100" />
        <el-table-column prop="customerName" label="客户" min-width="100" />
        <el-table-column prop="ceremonyTypeName" label="仪式类型" min-width="100" />
        <el-table-column prop="hallNo" label="仪式厅" width="100" />
        <el-table-column prop="startTime" label="开始时间" min-width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type as any" effect="light">
              {{ statusMap[row.status]?.text || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-button v-if="row.status === 0" type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 0" type="success" link @click="handleStart(row)">开始</el-button>
            <el-button v-if="row.status === 1" type="success" link @click="handleComplete(row)">完成</el-button>
            <el-button v-if="row.status === 0" type="danger" link @click="handleCancel(row)">取消</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px" @close="handleClose">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="关联订单" prop="orderId">
          <el-select v-model="formData.orderId" placeholder="请选择订单" style="width: 100%">
            <el-option v-for="item in orderOptions" :key="item.id" :label="item.orderNo + ' - ' + item.customerName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="仪式类型" prop="ceremonyType">
          <el-select v-model="formData.ceremonyType" placeholder="请选择仪式类型" style="width: 100%">
            <el-option v-for="(item, index) in ceremonyTypes" :key="index" :label="item" :value="index" />
          </el-select>
        </el-form-item>
        <el-form-item label="仪式厅" prop="hallNo">
          <el-select v-model="formData.hallNo" placeholder="请选择仪式厅" style="width: 100%">
            <el-option v-for="item in hallOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="formData.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="formData.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="formData.contactName" placeholder="联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="联系电话" />
        </el-form-item>
        <el-form-item label="参加人数" prop="attendeeCount">
          <el-input-number v-model="formData.attendeeCount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="特殊要求" prop="specialRequests">
          <el-input v-model="formData.specialRequests" type="textarea" :rows="2" placeholder="特殊要求" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="仪式详情" width="550px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="宠物">{{ detailData.petName }}</el-descriptions-item>
        <el-descriptions-item label="客户">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="仪式类型">{{ detailData.ceremonyTypeName }}</el-descriptions-item>
        <el-descriptions-item label="仪式厅">{{ detailData.hallNo }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detailData.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detailData.endTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detailData.contactName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.contactPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="参加人数">{{ detailData.attendeeCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detailData.status!]?.type as any">{{ statusMap[detailData.status!]?.text }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="特殊要求" :span="2">{{ detailData.specialRequests || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.ceremony-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; color: var(--text-color-primary); margin: 0; }
.card { background-color: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
.search-bar { display: flex; align-items: center; }
.pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 20px; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
