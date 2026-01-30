<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, View } from '@element-plus/icons-vue'
import { getCustomerList, createCustomer, updateCustomer, deleteCustomer, type Customer } from '@/api/customer'

// 搜索关键词
const searchKeyword = ref('')
const loading = ref(false)

// 客户列表数据
const customers = ref<Customer[]>([])
const total = ref(0)

// 分页参数
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 弹窗控制
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const detailVisible = ref(false)

// 表单数据
const formData = reactive<Customer>({
  name: '',
  phone: '',
  email: '',
  address: '',
  idCard: '',
  remark: ''
})

// 详情数据
const detailData = ref<Customer | null>(null)

// 表单引用
const formRef = ref()

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

// 获取客户列表
const fetchCustomers = async () => {
  loading.value = true
  try {
    const res = await getCustomerList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value
    })
    customers.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取客户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchCustomers()
}

// 新增客户
const handleAdd = () => {
  dialogTitle.value = '新增客户'
  resetForm()
  dialogVisible.value = true
}

// 编辑客户
const handleEdit = (row: Customer) => {
  dialogTitle.value = '编辑客户'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 查看详情
const handleDetail = (row: Customer) => {
  detailData.value = row
  detailVisible.value = true
}

// 删除客户
const handleDelete = async (row: Customer) => {
  try {
    await ElMessageBox.confirm('确定要删除该客户吗？删除后无法恢复。', '提示', {
      type: 'warning'
    })
    await deleteCustomer(row.id!)
    ElMessage.success('删除成功')
    fetchCustomers()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (formData.id) {
      await updateCustomer(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createCustomer(formData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchCustomers()
  } catch (error: any) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

// 重置表单
const resetForm = () => {
  formData.id = undefined
  formData.name = ''
  formData.phone = ''
  formData.email = ''
  formData.address = ''
  formData.idCard = ''
  formData.remark = ''
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page
  fetchCustomers()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchCustomers()
}

onMounted(() => {
  fetchCustomers()
})
</script>

<template>
  <div class="customer-container">
    <div class="page-header">
      <h1 class="page-title">客户管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增客户
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索客户姓名/手机号"
          clearable
          style="width: 300px"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" style="margin-left: 16px" @click="handleSearch">搜索</el-button>
      </div>

      <el-table :data="customers" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="客户姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="150" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              <el-icon><Delete /></el-icon> 删除
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="formData.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入地址" />
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

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="客户详情"
      width="500px"
    >
      <el-descriptions :column="1" border v-if="detailData">
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailData.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身份证">{{ detailData.idCard || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detailData.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createdAt }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.customer-container {
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
