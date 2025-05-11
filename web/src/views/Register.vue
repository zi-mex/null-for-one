<template>
  <div class="main-context">
    <el-card class="box-card">
      <el-space direction="vertical" style="width: 100%" size="large">
        <el-space>
<!--          <img src="../assets/logo.png" width="100%" style="width: 55px">-->
          <el-space direction="vertical" style="width: 100%" size="small">
            <h2 style="font-style: oblique">宠物综合商城</h2>
<!--            <span style="font-style: oblique;font-size: 15px">zimex.com</span>-->
          </el-space>
        </el-space>
        <el-form :model="formData" label-width="80px" :rules="rules" ref="formRef" style="width: 100%" >

          <el-row>
            <el-col :span="12">
              <el-form-item label="头像" prop="avatarUrl"
                            style="width: 100%"
                            :rules="[{required:true,message:'请选择头像',trigger:[ 'blur','change']}]">
                <MyUpLoad type="imageCard" :limit="1" :files="formData.avatarUrl"

                          @setFiles="formData.avatarUrl =$event"></MyUpLoad>
              </el-form-item>
              <el-form-item label="用户类型" prop="type"
                            :rules="[{required:true,message:'请选择类型',trigger:[ 'blur','change']}]">
                <el-select v-model="formData.type" placeholder="请选择用户类型" style="width: 180px">
                  <!--              注意：管理员不能注册-->
                  <el-option label="用户" value="USER"></el-option>
                  <el-option label="宠物店长" value="PET_STORE_MANAGER"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="用户名" prop="username"
                            :rules="[{required:true,message:'请输入用户名',trigger:[ 'blur','change']}]">
                <el-input
                    style="width: 180px"
                    placeholder="请输入用户名"
                    v-model.trim="formData.username"
                    clearable
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="店铺图片" prop="storeImg"
                            v-if="formData.type==='PET_STORE_MANAGER'"
                            :rules="[{required:true,message:'请选择店铺图片',trigger:['blur','change']}]"
                            style="width: 100%">
                <MyUpLoad type="imageCard" :limit="1" :files="formData.storeImg"
                          @setFiles="formData.storeImg=$event">
                </MyUpLoad>
              </el-form-item>
              <el-form-item label="密码" prop="password"
                            :rules="[{required:true,message:'请输入密码',trigger:[ 'blur','change']}]">
                <el-input
                    style="width: 180px"
                    placeholder="请输入密码"
                    show-password
                    v-model.trim="formData.password"
                    clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item label="昵称" prop="nickname"
                            :rules="[{required:true,message:'请输入昵称',trigger:[ 'blur','change']}]">
                <el-input
                    style="width: 180px"
                    placeholder="请输入昵称"
                    v-model.trim="formData.nickname"
                    clearable
                >
                </el-input>
              </el-form-item>
              <!--          v-if 是否显示
              :rules 规则
              -->
              <el-form-item label="店铺名称" prop="storeName"
                            v-if="formData.type==='PET_STORE_MANAGER'"
                            :rules="[{required:true,message:'请输入店铺名称',trigger:['blur','change']}]">
                <el-input style="width: 180px"
                          placeholder="请输入店铺名称"
                          v-model.trim="formData.storeName"
                          clearable>
                </el-input>
              </el-form-item>
              <el-form-item label="店铺地址" prop="storeAddress"
                            v-if="formData.type==='PET_STORE_MANAGER'"
                            :rules="[{required:true,message:'请输入店铺地址',trigger:['blur','change']}]">
                <el-input style="width: 180px"
                          placeholder="请输入店铺地址"
                          v-model.trim="formData.storeAddress"
                          clearable>
                </el-input>
              </el-form-item>

            </el-col>
          </el-row>




<!--          /**-->
<!--          * 店铺名称-->
<!--          */-->
<!--          private String storeName;-->
<!--          /**-->
<!--          * 店铺图片-->
<!--          */-->
<!--          private String storeImg;-->
<!--          /**-->
<!--          * 店铺地址-->
<!--          */-->
<!--          private String storeAddress;-->

          <el-form-item label="" style="width: 100%">
            <el-space direction="vertical" alignment="left" style="width: 100%">
              <el-button @click="submitForm()" type="success" style="width: 100%">注 册</el-button>
              <router-link tag="span" :to="{path:'login'}">
                <span style="float: right">已有账号？去登录</span>
              </router-link>
            </el-space>

          </el-form-item>
        </el-form>
      </el-space>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";
import MyUpLoad from "@/components/MyUpload.vue";
import router from "@/router/index.js";

const formRef = ref(null);
const formData = ref({
  type: 'USER',
  username: '',
  nickname: '',
  avatarUrl: '',
  password: ''
});

const rules = ref({
  username: [
    {required: true, message: '请输入用户名称', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
});

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      return
    }
    http.put("/common/register", formData.value).then(res => {
      if (!res) {
        return
      }
      ElMessage({
        message: "注册成功，正在跳转",
        type: "success"
      });
      router.push({path: "/login"})
    });
  });
}

</script>

<style scoped>
.main-context {
  height: 100vh; /* 使 .app 高度为视口高度 */
  background: url("../assets/pet-store.png") no-repeat center center fixed;
  background-size: cover; /* 使用 cover 保持图片比例 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: white; /* 根据背景图片调整文字颜色 */
}

.box-card {
  width: 700px;
  margin: 0 auto;
  text-align: center;
}
</style>
