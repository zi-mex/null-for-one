<template>
  <el-container class="my-container">
    <el-header class="my-header">
      <el-row class="nav">
        <el-col :span="4" class="logo">
          <div style="text-align: center;padding-top: 10px" @click="router.push('/')">
            <h1 style="color: #f9f9f9">宠物综合商城</h1>
          </div>
        </el-col>
        <el-col :span="16">
          <div style="text-align: center">
            <el-menu
                :default-active="useRoute().path"
                mode="horizontal"
                router
                @select="handleSelect"
                text-color="white"
                active-text-color="white"
                background-color="RGB(100,120,200)"
            >
              <el-menu-item index="/admin/home"   v-if="currentUser.type==='ADMIN'">
                <el-icon>
                  <Lock/>
                </el-icon>
                <span>首页</span>
              </el-menu-item>
              <el-menu-item index="/myPet">我的宠物</el-menu-item>
              <el-menu-item index="/petDiary">宠物日记</el-menu-item>
              <el-menu-item index="/helpMessage">求助信息</el-menu-item>
              <el-menu-item index="/productList">宠物商城</el-menu-item>
              <el-menu-item index="/petStore">查找店铺</el-menu-item>
              <el-menu-item index="/personalCenter">个人中心</el-menu-item>
            </el-menu>
          </div>
        </el-col>
        <el-col :span="4">
          <div style="text-align: center;">
            <el-space style="margin-top: 15px;">
              <el-dropdown v-if="isUserLogin">
                <div>
                  <el-space>
                    <el-avatar style="width: 30px;height: 30px;border-radius: 50%"
                               shape="square" :size="30" :src="currentUser.avatarUrl"></el-avatar>
                    <span style="font-size: 16px;color: #f9f9f9">  {{ currentUser.username }}</span></el-space>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item><span @click="editCurrentUser">个人信息</span></el-dropdown-item>
                    <el-dropdown-item><span @click="editPassword">修改密码</span></el-dropdown-item>
                    <el-dropdown-item><span @click="balanceInfo">余额/充值</span></el-dropdown-item>
                    <el-dropdown-item divided><span @click="logout">退出登录</span></el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-space>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-main class="my-main">
      <router-view/>
    </el-main>
    <el-footer class="my-footer">
      <p>计算机232</p>
      <p>by zimex </p>
    </el-footer>
  </el-container>
</template>

<script setup>
import tools from "@/utils/tools.js";
import {ref} from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {useRoute} from "vue-router";
import {Lock} from "@element-plus/icons-vue";

const isUserLogin = ref(tools.isLogin())
const currentUser = ref(tools.getCurrentUser())

if(currentUser.value===null){
  window.location.href="/login"
}
if (currentUser.value && currentUser.value.type !== 'USER') {
  router.push({path: "/admin"})
}

function handleSelect(key, keyPath) {
  console.log(key, keyPath);
}

function logout() {
  ElMessage({
    message: '退出登录成功，正在跳转',
    type: 'success'
  });
  localStorage.clear()
  router.push({path: "/login"})
}

function editCurrentUser() {
  router.push({path: "/editCurrentUser"})
}

function editPassword() {
  router.push({path: "/editPassword"})
}

function balanceInfo() {
  router.push({path: "/balanceInfo"})
}

</script>


<style scoped>
.my-container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 例如，设置为视窗的高度 */
}

.my-header {
  background-color:RGB(100,120,200)
}

.el-menu.el-menu--horizontal {
  border-bottom: none;
}

.my-main {
  /* flex-grow: 1;  让子元素占据剩余空间 */
  background: rgb(245, 245, 245);
}

.my-footer {
  font-size: 14px;
  padding: 10px;
  color: #999;
  background-color: white;
  text-align: center;
}
</style>
