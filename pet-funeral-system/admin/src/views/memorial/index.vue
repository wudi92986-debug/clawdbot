<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, View } from '@element-plus/icons-vue'
import { getMemorialList, getMemorialDetail, getMemorialMessages, type Memorial } from '@/api/memorial'

const loading = ref(false)
const searchStatus = ref<number | ''>('')

const memorials = ref<Memorial[]>([])
const total = ref(0)

const pagination = reactive({
  page: 1,
  pageSize: 10
})

const detailVisible = ref(false)
const messagesVisible = ref(false)
const detailData = ref<Memorial | null>(null)
const messages = ref<any[]>([])

const statusMap: Record<number, { text: string; type: string }> = {
  0: { text: '待审核', type: 'warning' },
  1: { text: '已上线', type: 'success' },
  2: { text: '已下线', type: 'info' }
}

const fetchMemorials = async () => {
  loading.value = true
  try {
    const res = await getMemorialList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      status: searchStatus.value === '' ? undefined : searchStatus.value
    })
    memorials.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchMemorials()
}

const handleDetail = async (row: Memorial) => {
  try {
    const res = await getMemorialDetail(row.id!)
    detailData.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

const handleViewMessages = async (row: Memorial) => {
  try {
    const res = await getMemorialMessages(row.id!, 50)
    messages.value = res.data || []
    messagesVisible.value = true
  } catch (error) {
    console.error('获取留言失败:', error)
  }
}

const handlePageChange = (page: number) => {
  pagination.page = page
  fetchMemorials()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  fetchMemorials()
}

onMounted(() => {
  fetchMemorials()
})
</script>

<template>
  <div class="memorial-container">
    <div class="page-header">
      <h1 class="page-title">纪念馆管理</h1>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-select v-model="searchStatus" placeholder="状态筛选" clearable style="width: 150px">
          <el-option v-for="(item, key) in statusMap" :key="key" :label="item.text" :value="Number(key)" />
        </el-select>
        <el-button type="primary" style="margin-left: 16px" @click="handleSearch">搜索</el-button>
      </div>

      <el-table :data="memorials" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column prop="petName" label="宠物" min-width="100" />
        <el-table-column prop="customerName" label="创建者" min-width="100" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="candleCount" label="点烛" width="80" />
        <el-table-column prop="flowerCount" label="献花" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type as any" effect="light">
              {{ statusMap[row.status]?.text || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
            <el-button type="primary" link @click="handleViewMessages(row)">留言</el-button>
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="纪念馆详情" width="600px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ detailData.title }}</el-descriptions-item>
        <el-descriptions-item label="宠物">{{ detailData.petName }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ detailData.viewCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="点烛数">{{ detailData.candleCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="献花数">{{ detailData.flowerCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detailData.status!]?.type as any">{{ statusMap[detailData.status!]?.text }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ detailData.createdAt }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 留言弹窗 -->
    <el-dialog v-model="messagesVisible" title="纪念馆留言" width="600px">
      <div v-if="messages.length === 0" style="text-align: center; color: #999; padding: 40px;">
        暂无留言
      </div>
      <div v-else class="message-list">
        <div v-for="msg in messages" :key="msg.id" class="message-item">
          <div class="message-header">
            <span class="message-nickname">{{ msg.nickname || '匿名' }}</span>
            <span class="message-time">{{ msg.createdAt }}</span>
          </div>
          <div class="message-content">{{ msg.content }}</div>
        </div>
      </div>
      <template #footer>
        <el-button @click="messagesVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.memorial-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; color: var(--text-color-primary); margin: 0; }
.card { background-color: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
.search-bar { display: flex; align-items: center; }
.pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 20px; }

.message-list {
  max-height: 400px;
  overflow-y: auto;
}

.message-item {
  padding: 12px;
  border-bottom: 1px solid #eee;
  
  &:last-child {
    border-bottom: none;
  }
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.message-nickname {
  font-weight: 500;
  color: #333;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-content {
  color: #666;
  line-height: 1.6;
}

@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
