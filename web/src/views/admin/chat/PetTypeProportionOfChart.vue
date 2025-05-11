<template>
  <el-space direction="vertical" style="width: 100%" size="large">
    <div ref="chart" style="width: 400px; height: 345px;"></div>
  </el-space>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue';
import * as echarts from 'echarts';
import http from "@/utils/http.js";

const chart = ref(null);

onBeforeUnmount(() => {
  // 清理图表实例
  if (chart.value) {
    chart.value.dispose();
  }
});

// 指定图表的配置项和数据
const option = {
  title: {
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量',
      type: 'pie',
      radius: '50%',
      data: [],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};





const initChart = () => {
  // 基于准备好的dom，初始化echarts实例
  const chartInstance = echarts.init(chart.value);


  // 使用刚指定的配置项和数据显示图表
  chartInstance.setOption(option);

  // 将 chart 实例绑定到 ref 上，以便在卸载时清理
  chart.value = chartInstance;
};



getData()

function getData() {
  http.get("/statisticalReportForms/petTypeProportionOfChar").then(res => {
    option.series[0].data = res.data
    option.title.text = '宠物类型占比'
    initChart();
  })
}


</script>

<style scoped>
/* 可以添加样式 */
</style>
