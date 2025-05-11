<template>
  <div class="not-found">
    <el-card class="card" shadow="hover">
      <h1 class="error-code">404</h1>
      <h2 class="error-message">页面未找到</h2>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
  methods: {
    goHome() {
      this.$router.push({ path: '/' }); // 替换为你的主页路由
    }
  }
}
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f9f9f9;
}

.card {
  text-align: center;
  padding: 20px;
  border-radius: 10px;
}

.error-code {
  font-size: 100px;
  margin: 0;
  color: #f56c6c; /* 红色 */
}

.error-message {
  font-size: 24px;
  margin: 10px 0;
  color: #606266; /* 深灰色 */
}
</style>