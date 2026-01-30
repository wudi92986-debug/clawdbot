<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getPackageList, createPackage, updatePackage, deletePackage, type ServicePackage } from '@/api/package'

// 搜索关键词
const searchKeyword = ref('')
const loading = ref(false)

// 套餐列表数据
const packages = ref<ServicePackage[]>([])
const total = ref(0)

// 分页参数
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 弹窗控制
const dialogVisible = ref(false)
const dialogTitle = ref('新增套餐')

// 表单数据
const formData = reactive<ServicePackage>({
  name: '',
  description: '',
  price: 0,
  originalPrice: 0,
  duration: 0,
  items: '',
  sort: 0,
  status: 1
})

// 表单引用
const formRef = ref()

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

// 获取套餐列表
const fetchPackages = async () => {
  loading.value = true
  try {
    const res = await getPackageList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value
    })
    packages.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取套餐列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchPackages()
}

// 新增套餐
const handleAdd = () => {
  dialogTitle.value = '新增套餐'
  resetForm()
  dialogVisible.value = true
}

// 编辑套餐
const handleEdit = (row: ServicePackage) => {
  dialogTitle.value = '编辑套餐'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除套餐
const handleDelete = async (row: ServicePackage) => {
  try {
    await ElMessageBox.confirm('确定要删除该套餐吗？', '提示', {
      type: 'warning'
    })
    await deletePackage(row.id!)
    ElMessage.success('删除成功')
    fetchPackages()
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
      await updatePackage(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createPackage(formData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchPackages()
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
  formData.description = ''
  formData.price = 0
  formData.originalPrice = 0
  formData.duration = 0
  formData.items = ''
  formData.sort = 0
  formData.status = 1
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page
  fetchPackages()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchPackages()
}

onMounted(() => {
  fetchPackages()
})
</script>

<template>
  <div class="service-container">
    <div class="page-header">
      <h1 class="page-title">服务套餐管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增套餐
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索套餐名称"
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

      <el-table :data="packages" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="套餐名称" min-width="150" />
        <el-table-column prop="price" label="价格" min-width="100">
          <template #default="{ row }">
            <span style="color: #E8B89D; font-weight: 600">¥{{ row.price?.toLocaleString() || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="originalPrice" label="原价" min-width="100">
          <template #default="{ row }">
            <span style="text-decoration: line-through; color: #999">
              ¥{{ row.originalPrice?.toLocaleString() || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="light">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
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
      width="600px"
      @close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="套餐名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="formData.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原价" prop="originalPrice">
              <el-input-number v-model="formData.originalPrice" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="时长(分)" prop="duration">
              <el-input-number v-model="formData.duration" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="formData.sort" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="请输入套餐描述" />
        </el-form-item>
        <el-form-item label="服务项目" prop="items">
          <el-input v-model="formData.items" type="textarea" :rows="4" placeholder="请输入服务项目，每行一项" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.service-container {
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
