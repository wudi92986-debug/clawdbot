<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const activeTab = ref('package')

// 套餐列表
const packages = ref([
  { id: 1, name: '基础告别套餐', price: 1280, desc: '集体火化，不保留骨灰', icon: '🌿' },
  { id: 2, name: '温馨告别套餐', price: 2980, desc: '含告别仪式+单独火化', icon: '🌸' },
  { id: 3, name: '尊享告别套餐', price: 5980, desc: 'VIP全套服务+纪念相册', icon: '💫' },
])

// 单项服务
const services = ref([
  { id: 1, name: '上门接运', price: 200, unit: '次' },
  { id: 2, name: '遗容整理', price: 300, unit: '次' },
  { id: 3, name: '告别仪式', price: 800, unit: '30分钟' },
  { id: 4, name: '单独火化', price: 1200, unit: '次' },
  { id: 5, name: '骨灰盒', price: 480, unit: '个' },
  { id: 6, name: '骨灰寄存', price: 1200, unit: '年' },
])

// 纪念品
const products = ref([
  { id: 1, name: '骨灰晶石吊坠', price: 1280, image: '' },
  { id: 2, name: 'AI宠物画像', price: 399, image: '' },
  { id: 3, name: '毛发琥珀吊坠', price: 880, image: '' },
  { id: 4, name: '3D打印摆件', price: 599, image: '' },
])

const goBooking = (packageId?: number) => {
  router.push(packageId ? `/booking?package=${packageId}` : '/booking')
}
</script>

<template>
  <div class="service-page page">
    <div class="page-header">
      <h1>服务项目</h1>
    </div>

    <van-tabs v-model:active="activeTab" sticky swipeable>
      <van-tab title="服务套餐" name="package">
        <div class="tab-content">
          <div
            v-for="pkg in packages"
            :key="pkg.id"
            class="package-card"
            @click="goBooking(pkg.id)"
          >
            <div class="package-icon">{{ pkg.icon }}</div>
            <div class="package-info">
              <h3>{{ pkg.name }}</h3>
              <p>{{ pkg.desc }}</p>
            </div>
            <div class="package-price">
              <span class="currency">¥</span>
              <span class="amount">{{ pkg.price }}</span>
              <span class="unit">起</span>
            </div>
          </div>
        </div>
      </van-tab>

      <van-tab title="单项服务" name="service">
        <div class="tab-content">
          <van-cell-group inset>
            <van-cell
              v-for="service in services"
              :key="service.id"
              :title="service.name"
              :label="'¥' + service.price + ' / ' + service.unit"
              is-link
              @click="goBooking()"
            />
          </van-cell-group>
        </div>
      </van-tab>

      <van-tab title="纪念品商城" name="shop">
        <div class="tab-content">
          <div class="product-grid">
            <div v-for="product in products" :key="product.id" class="product-card">
              <div class="product-image">🎁</div>
              <div class="product-name">{{ product.name }}</div>
              <div class="product-price">¥{{ product.price }}</div>
            </div>
          </div>
        </div>
      </van-tab>

      <van-tab title="临终关怀" name="care">
        <div class="tab-content">
          <van-cell-group inset>
            <van-cell title="如何判断宠物是否进入临终期" is-link />
            <van-cell title="陪伴宠物走完最后一程" is-link />
            <van-cell title="宠物离世后的心理调适" is-link />
            <van-cell title="如何向孩子解释宠物的离去" is-link />
          </van-cell-group>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<style lang="scss" scoped>
.service-page {
  .page-header {
    background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
    color: #fff;
    padding: 20px 16px;
    padding-top: calc(20px + env(safe-area-inset-top));

    h1 {
      font-size: 20px;
      margin: 0;
    }
  }
}

.tab-content {
  padding: 16px;
}

.package-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 12px;

  .package-icon {
    font-size: 36px;
  }

  .package-info {
    flex: 1;

    h3 {
      font-size: 16px;
      margin: 0 0 4px;
    }

    p {
      font-size: 12px;
      color: var(--text-color-secondary);
      margin: 0;
    }
  }

  .package-price {
    .currency {
      font-size: 12px;
      color: var(--color-warning);
    }

    .amount {
      font-size: 20px;
      font-weight: 700;
      color: var(--color-warning);
    }

    .unit {
      font-size: 12px;
      color: var(--text-color-secondary);
    }
  }
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.product-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  text-align: center;

  .product-image {
    font-size: 48px;
    margin-bottom: 12px;
  }

  .product-name {
    font-size: 14px;
    margin-bottom: 8px;
  }

  .product-price {
    font-size: 16px;
    font-weight: 600;
    color: var(--color-warning);
  }
}
</style>
