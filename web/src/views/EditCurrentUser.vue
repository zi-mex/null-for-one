<template>
  <div style="width:600px;margin: 0 auto">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人信息修改</span>
        </div>
      </template>
      <el-form :model="formData" label-width="100px">
        <el-form-item prop="img" label="头像">
          <MyUpLoad type="imageCard" :limit="1" :files="formData.avatarUrl"
                    @setFiles="formData.avatarUrl =$event"></MyUpLoad>
        </el-form-item>
        <el-form-item prop="username" label="用户名">
          <el-input type="text"
                    v-model="formData.username"
                    auto-complete="off"
                    placeholder="用户名"
                    :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item prop="nickname" label="昵称">
          <el-input type="text"
                    v-model="formData.nickname"
                    auto-complete="off"
                    placeholder="用户名"
                    :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input
              v-model="formData.email"
              auto-complete="off"
              placeholder="邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item prop="tel" label="电话">
          <el-input type="text"
                    v-model="formData.tel"
                    auto-complete="off"
                    placeholder="电话"
          ></el-input>
        </el-form-item>
        <el-form-item label="店铺名称" prop="storeName"
                      v-if="formData.type === 'PET_STORE_MANAGER'">
          <el-input
              placeholder="请输入店铺名称"
              v-model.trim="formData.storeName"
              clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item label="店铺地址" prop="storeAddress"
                      v-if="formData.type === 'PET_STORE_MANAGER'">
          <el-input
              placeholder="请输入店铺地址"
              v-model.trim="formData.storeAddress"
              clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item label="店铺图片" prop="storeImg"
                      v-if="formData.type === 'PET_STORE_MANAGER'">
          <MyUpLoad type="imageCard" :limit="1" :files="formData.storeImg"

                    @setFiles="formData.storeImg =$event"></MyUpLoad>
        </el-form-item>

        <el-form-item style="width:100%;">
          <el-button type="primary" style="width:100px;" @click="handleSubmit">修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import utils from "@/utils/tools.js";
import MyUpLoad from "@/components/MyUpload.vue";
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";

const formData = ref({});

load();

function load() {
  formData.value = utils.getCurrentUser();
}


function handleSubmit() {
  http.post('/common/updateCurrentUser', formData.value).then(res => {
    http.get("/common/currentUser").then(res1 => {
      let currentUser = res1.data;
      localStorage.setItem("currentUser", JSON.stringify(currentUser));
      ElMessage({
        message: '修改成功',
        type: 'success'
      });
    })
  });
};
</script>
