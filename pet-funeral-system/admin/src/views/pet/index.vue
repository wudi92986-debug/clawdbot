<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, View } from '@element-plus/icons-vue'
import { getPetList, createPet, updatePet, deletePet, registerDeath, type Pet } from '@/api/pet'
import { getCustomerList } from '@/api/customer'

// 搜索关键词
const searchKeyword = ref('')
const loading = ref(false)

// 宠物列表数据
const pets = ref<Pet[]>([])
const total = ref(0)

// 客户列表（用于选择）
const customerOptions = ref<any[]>([])

// 分页参数
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 弹窗控制
const dialogVisible = ref(false)
const dialogTitle = ref('新增宠物')
const detailVisible = ref(false)
const deathDialogVisible = ref(false)

// 表单数据
const formData = reactive<Pet>({
  customerId: 0,
  name: '',
  species: '',
  breed: '',
  color: '',
  age: undefined,
  weight: undefined,
  gender: 0,
  remark: ''
})

// 死亡登记表单
const deathForm = reactive({
  petId: 0,
  deathTime: '',
  deathCause: ''
})

// 详情数据
const detailData = ref<Pet | null>(null)

// 表单引用
const formRef = ref()

// 物种选项
const speciesOptions = ['狗', '猫', '兔子', '仓鼠', '鸟类', '鱼类', '其他']

// 状态映射
const statusMap: Record<number, { text: string; type: string }> = {
  0: { text: '健康', type: 'success' },
  1: { text: '已故', type: 'info' },
  2: { text: '服务中', type: 'warning' }
}

// 表单验证规则
const rules = {
  customerId: [{ required: true, message: '请选择所属客户', trigger: 'change' }],
  name: [{ required: true, message: '请输入宠物名称', trigger: 'blur' }],
  species: [{ required: true, message: '请选择宠物类型', trigger: 'change' }]
}

// 获取宠物列表
const fetchPets = async () => {
  loading.value = true
  try {
    const res = await getPetList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value
    })
    pets.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取宠物列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取客户列表
const fetchCustomers = async () => {
  try {
    const res = await getCustomerList({ pageSize: 1000 })
    customerOptions.value = res.data?.records || []
  } catch (error) {
    console.error('获取客户列表失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchPets()
}

// 新增宠物
const handleAdd = () => {
  dialogTitle.value = '新增宠物'
  resetForm()
  dialogVisible.value = true
}

// 编辑宠物
const handleEdit = (row: Pet) => {
  dialogTitle.value = '编辑宠物'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 查看详情
const handleDetail = (row: Pet) => {
  detailData.value = row
  detailVisible.value = true
}

// 删除宠物
const handleDelete = async (row: Pet) => {
  try {
    await ElMessageBox.confirm('确定要删除该宠物信息吗？', '提示', {
      type: 'warning'
    })
    await deletePet(row.id!)
    ElMessage.success('删除成功')
    fetchPets()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 打开死亡登记弹窗
const handleDeathRegister = (row: Pet) => {
  deathForm.petId = row.id!
  deathForm.deathTime = ''
  deathForm.deathCause = ''
  deathDialogVisible.value = true
}

// 提交死亡登记
const submitDeath = async () => {
  if (!deathForm.deathTime) {
    ElMessage.warning('请选择死亡时间')
    return
  }
  try {
    await registerDeath(deathForm.petId, {
      deathTime: deathForm.deathTime,
      deathCause: deathForm.deathCause
    })
    ElMessage.success('登记成功')
    deathDialogVisible.value = false
    fetchPets()
  } catch (error) {
    console.error('登记失败:', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (formData.id) {
      await updatePet(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createPet(formData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchPets()
  } catch (error: any) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

// 重置表单
const resetForm = () => {
  formData.id = undefined
  formData.customerId = 0
  formData.name = ''
  formData.species = ''
  formData.breed = ''
  formData.color = ''
  formData.age = undefined
  formData.weight = undefined
  formData.gender = 0
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
  fetchPets()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchPets()
}

onMounted(() => {
  fetchPets()
  fetchCustomers()
})
</script>

<template>
  <div class="pet-container">
    <div class="page-header">
      <h1 class="page-title">宠物管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增宠物
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索宠物名称/主人姓名"
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

      <el-table :data="pets" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="宠物名称" min-width="100" />
        <el-table-column prop="species" label="类型" min-width="80" />
        <el-table-column prop="breed" label="品种" min-width="100" />
        <el-table-column prop="customerName" label="主人" min-width="100" />
        <el-table-column prop="age" label="年龄" width="80">
          <template #default="{ row }">
            {{ row.age ? row.age + '岁' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type as any" effect="light">
              {{ statusMap[row.status]?.text || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="登记时间" min-width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              type="warning" 
              link 
              @click="handleDeathRegister(row)"
            >
              死亡登记
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
        <el-form-item label="所属客户" prop="customerId">
          <el-select v-model="formData.customerId" placeholder="请选择客户" style="width: 100%">
            <el-option
              v-for="item in customerOptions"
              :key="item.id"
              :label="item.name + ' - ' + item.phone"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="宠物名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入宠物名称" />
        </el-form-item>
        <el-form-item label="宠物类型" prop="species">
          <el-select v-model="formData.species" placeholder="请选择宠物类型" style="width: 100%">
            <el-option v-for="item in speciesOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="品种" prop="breed">
          <el-input v-model="formData.breed" placeholder="请输入品种" />
        </el-form-item>
        <el-form-item label="毛色" prop="color">
          <el-input v-model="formData.color" placeholder="请输入毛色" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :value="0">未知</el-radio>
            <el-radio :value="1">公</el-radio>
            <el-radio :value="2">母</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="formData.age" :min="0" :max="100" placeholder="年龄" />
        </el-form-item>
        <el-form-item label="体重(kg)" prop="weight">
          <el-input-number v-model="formData.weight" :min="0" :precision="2" placeholder="体重" />
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
      title="宠物详情"
      width="500px"
    >
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="宠物名称">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ detailData.species }}</el-descriptions-item>
        <el-descriptions-item label="品种">{{ detailData.breed || '-' }}</el-descriptions-item>
        <el-descriptions-item label="毛色">{{ detailData.color || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          {{ detailData.gender === 1 ? '公' : detailData.gender === 2 ? '母' : '未知' }}
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailData.age ? detailData.age + '岁' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="体重">{{ detailData.weight ? detailData.weight + 'kg' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="主人">{{ detailData.customerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detailData.status!]?.type as any" effect="light">
            {{ statusMap[detailData.status!]?.text || '未知' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="死亡时间" v-if="detailData.status === 1">
          {{ detailData.deathTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="死亡原因" v-if="detailData.status === 1">
          {{ detailData.deathCause || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="登记时间">{{ detailData.createdAt }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 死亡登记弹窗 -->
    <el-dialog
      v-model="deathDialogVisible"
      title="死亡登记"
      width="400px"
    >
      <el-form label-width="80px">
        <el-form-item label="死亡时间" required>
          <el-date-picker
            v-model="deathForm.deathTime"
            type="datetime"
            placeholder="选择死亡时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="死亡原因">
          <el-input
            v-model="deathForm.deathCause"
            type="textarea"
            :rows="3"
            placeholder="请输入死亡原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="deathDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDeath">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.pet-container {
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
