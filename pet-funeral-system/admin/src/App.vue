<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <el-config-provider :locale="zhCn">
    <RouterView />
  </el-config-provider>
</template>

<script lang="ts">
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
export default {
  data() {
    return { zhCn }
  }
}
</script>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>
