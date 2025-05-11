<template>

  <el-space direction="vertical" style="width: 100%" size="large">
    <el-radio-group v-model="day" @change="getData">
      <el-radio :value="3">3天</el-radio>
      <el-radio :value="7">7天</el-radio>
      <el-radio :value="14">14天</el-radio>
      <el-radio :value="30">30天</el-radio>
    </el-radio-group>
    <div ref="chart" style="width:800px; height: 300px;"></div>
  </el-space>
</template>

<script setup>
import {onBeforeUnmount, ref} from 'vue';
import * as echarts from 'echarts';
import http from "@/utils/http.js";

const chart = ref(null);


onBeforeUnmount(() => {
  // 清理图表实例
  if (chart.value) {
    chart.value.dispose();
  }
});




var option = {
  title: {
    text: '售额趋势'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: []
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: [
    {
      type: 'value',
      position: 'left',
      axisLine: {
        lineStyle: {
          color: '#5793f3'
        }
      },
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    {
      type: 'value',
      position: 'right',
      axisLine: {
        lineStyle: {
          color: '#d14a61'
        }
      },
      splitLine: {
        show: false
      }
    }
  ],
  series: [
    {
      type: 'bar',
      data: [120, 200, 150, 80, 70, 110, 130],
      itemStyle: {
        color: '#5793f3'
      }
    },
    {
      type: 'line',
      yAxisIndex: 1,
      data: [30, 70, 50, 90, 60, 100, 80],
      itemStyle: {
        color: '#d14a61'
      },
      lineStyle: {
        width: 2
      },
      smooth: true
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

const day = ref(7)

getData()

function getData() {
  http.get("/statisticalReportForms/productSalesTotalAmountChart/" + day.value).then(res => {
    option.xAxis.data = res.data.xdata
    option.series[0].data = res.data.seriesData
    option.series[1].data = res.data.seriesData
    option.title.text = '最近' + day.value + '天销售额趋势'
    initChart();
  })
}

</script>

<style scoped>
/* 可以添加样式 */
</style>
