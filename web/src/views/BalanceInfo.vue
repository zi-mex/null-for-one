<template>
  <div style="width:600px;margin: 0 auto">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>余额/充值</span>
        </div>
      </template>
      <el-space direction="vertical" alignment="left">
        <el-statistic title="当前余额" :value="userInfo.balance"/>
        <el-button type="primary" @click="topUp">充值</el-button>
      </el-space>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import http from "@/utils/http.js";
import {ElMessage, ElMessageBox} from "element-plus";
const userInfo = ref({});

load();

function load() {
  http.get('/common/currentUser').then(res => {
    userInfo.value = res.data;
  })
}

function topUp() {
  ElMessageBox.prompt('请输入充值金额', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^[1-9]\d*$/, // 至少输入一个字符
    inputErrorMessage: '充值金额不能为空' // 当输入不满足正则表达式时显示的错误信息
  }).then(({value}) => {
    http.post('/user/topUp/' + value).then(res => {
      ElMessage({
        message: '充值成功',
        type: 'success'
      });
      load();
    });
  })
};
</script>
