<template>
  <div style="width:600px;margin: 0 auto">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>密码修改</span>
        </div>
      </template>
      <el-form :model="formData" label-width="100px"
      >
        <el-form-item prop="oldPassword" label="旧密码">
          <el-input type="password"
                    v-model="formData.oldPassword"
                    auto-complete="off"
                    placeholder="密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="newPassword" label="新密码">
          <el-input type="password"
                    v-model="formData.newPassword"
                    auto-complete="off"
                    placeholder="新密码"
          ></el-input>
        </el-form-item>
        <el-form-item style="width:100%;">
          <el-button type="primary" style="width:100px;" @click="handleSubmit">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import http from "@/utils/http.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const formData = ref({
  id: "",
  oldPassword: '',
  newPassword: '',
})

function handleSubmit() {
  http.post('/common/updatePassword', formData.value).then(res => {
    if (!res) {
      return
    }
    localStorage.clear()
    ElMessage({
      message: '修改成功请重新登录',
      type: 'success'
    });
    router.push({path: "/login"})
  })
}
</script>
