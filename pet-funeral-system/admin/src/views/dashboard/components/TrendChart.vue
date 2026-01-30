<script setup lang="ts">
import { ref, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
} from 'echarts/components'

use([
  CanvasRenderer,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
])

const chartOption = ref({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
    },
  },
  legend: {
    data: ['订单量', '收入(千元)'],
    top: 0,
    right: 0,
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    axisLine: {
      lineStyle: {
        color: '#E4E7ED',
      },
    },
    axisLabel: {
      color: '#909399',
    },
  },
  yAxis: [
    {
      type: 'value',
      name: '订单量',
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      splitLine: {
        lineStyle: {
          color: '#E4E7ED',
          type: 'dashed',
        },
      },
      axisLabel: {
        color: '#909399',
      },
    },
    {
      type: 'value',
      name: '收入(千元)',
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      splitLine: {
        show: false,
      },
      axisLabel: {
        color: '#909399',
      },
    },
  ],
  series: [
    {
      name: '订单量',
      type: 'line',
      smooth: true,
      data: [85, 92, 78, 105, 120, 135, 142, 138, 125, 148, 156, 168],
      itemStyle: {
        color: '#8B7355',
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(139, 115, 85, 0.3)' },
            { offset: 1, color: 'rgba(139, 115, 85, 0.05)' },
          ],
        },
      },
    },
    {
      name: '收入(千元)',
      type: 'bar',
      yAxisIndex: 1,
      data: [180, 195, 165, 220, 255, 285, 300, 292, 265, 315, 330, 356],
      itemStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: '#A8C686' },
            { offset: 1, color: '#C4D9A8' },
          ],
        },
        borderRadius: [4, 4, 0, 0],
      },
      barWidth: '40%',
    },
  ],
})
</script>

<template>
  <div class="trend-chart">
    <v-chart :option="chartOption" autoresize style="height: 350px" />
  </div>
</template>

<style lang="scss" scoped>
.trend-chart {
  width: 100%;
}
</style>
