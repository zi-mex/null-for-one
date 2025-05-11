<template>
  <div style="width: 75%;margin: 0 auto">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="hover" style="height: 470px;">
          <template #header>
            <span class="card-header-text">宠物店铺</span>
            <el-button class="card-header-more" type="text" @click="router.push('/petStore')">
              更多
            </el-button>
          </template>
          <el-row :gutter="20">
            <el-col style="cursor: pointer" :span="12" v-for="(item,index) in petStoreList" v-show="index===0" :key="item.id"
                    @click="router.push('/petStore?storeName='+item.storeName)">
              <div class="image-container">
                <img :src="item.storeImg"
                     style="height:300px"
                     @click="router.push(item.id)"/>
                <div class="overlay">{{ item.storeName }}</div>
              </div>
            </el-col>
            <el-col :span="12">
              <el-row :gutter="20">
                <el-col style="cursor: pointer" :span="12" v-for="(item,index) in petStoreList" v-show="index>0 && index<=4"
                        :key="item.id" @click="router.push('/petStore?storeName='+item.storeName)">
                  <div class="image-container" :style="{ marginTop: index < 3 ? '0' : '20px' }">
                    <img :src="item.storeImg"
                         style="height:140px"
                         @click="router.push(item.id)"/>
                    <div class="overlay">{{ item.storeName }}</div>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="12" v-for="(item,index) in petStoreList" v-show="index>4 && index<15"
                    @click="router.push('/petStore?storeName='+item.storeName)">
              <div :style="{ marginTop: index < 7 ? '0' : '10px' }" style="width: 100%;">
                <el-text style="cursor: pointer"  line-clamp="1" class="colored-text"> <el-icon><Place /></el-icon> {{ item.storeName }}</el-text>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="height: 470px;">
          <template #header>
            <span>求助信息</span>
            <el-button class="card-header-more" type="text" @click="router.push('/helpMessage')">
              更多
            </el-button>
          </template>
          <el-scrollbar height="400px" style="padding-right: 20px">
            <el-timeline style="max-width: 600px">
              <el-timeline-item :timestamp="item.createTime" placement="top" v-for="item in  helpMessageList">
                <el-card @click="router.push('/helpMessageDetails/'+item.id)" style="cursor:pointer">
                  <el-space direction="vertical" alignment="left">
                    <el-space>
                      <el-avatar :src="item.userAvatarUrl" class="user-avatar"></el-avatar>
                      <h5 class="username">{{ item.username }}</h5>
                    </el-space>
                    <div><h4>{{ item.title }}</h4></div>
                  </el-space>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card shadow="hover" style="height: 710px;">
          <template #header>
            <span class="card-header-text">宠物商城</span>
            <el-button class="card-header-more" type="text" @click="router.push('/productList')">
              更多
            </el-button>
          </template>
          <el-row :gutter="20">
            <el-col :span="6" v-for="(item,index) in productList" :key="item.id" style="margin-bottom: 20px;cursor: pointer">
              <el-card  shadow="hover" :style="index>5 ? { marginTop: '10px' } : null"
                        @click="router.push('/productDetails/'+item.id)">
                <template #header>
                  <el-text size="default" tag="b" line-clamp="1">
                    {{ item.name }}
                  </el-text>
                </template>
                <el-image style=" width: 100%;height: calc(0.69 * 10vw);" fit="fill"
                          :src="item.mainImg"
                />
                <template #footer>
                  <span style="color: red">￥{{ item.price }}</span>
                  <div style="float: right">
                    <el-space>
                      <el-text class="mx-1" type="warning">已售</el-text>
                      <el-text class="mx-1">{{ item.salesVolume }}</el-text>
                    </el-space>
                  </div>
                </template>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>宠物日记</span>
            <el-button class="card-header-more" type="text" @click="router.push('/petDiary')">
              更多
            </el-button>
          </template>
          <el-scrollbar height="610px" style="padding-right: 20px">
            <div :span="12" v-for="(item,index) in petDiaryList" @click="router.push('/petDiaryDetails/'+item.id)">
              <div :style="{ marginTop: index <1 ? '0' : '10px' }" style="width: 100%;cursor:pointer">
                <el-text line-clamp="1" size="large"><el-icon><Document /></el-icon> {{ item.title }}</el-text>
              </div>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>

import {ref} from 'vue'
import request from "@/utils/http.js";
import router from "@/router/index.js";
import {Document, Place} from "@element-plus/icons-vue";


//店铺
const petStoreList = ref([])
getPetStoreListList();

async function getPetStoreListList() {
  let data = {
    pageNum: 1,
    pageSize: 10
  }
  request.get("/petStoreManager/page", {
    params: data
  }).then(res => {
    petStoreList.value = res.data.list;
  })
}

//宠物日记
const petDiaryList = ref([])
getPetDiaryList();

async function getPetDiaryList() {
  let data = {
    pageNum: 1,
    pageSize: 20
  }
  request.get("/petDiary/page", {
    params: data
  }).then(res => {
    petDiaryList.value = res.data.list;
  })
}

//求助信息
const helpMessageList = ref([])
getHelpMessageList();

async function getHelpMessageList() {
  let data = {
    pageNum: 1,
    pageSize: 10
  }
  request.get("/helpMessage/page", {
    params: data
  }).then(res => {
    helpMessageList.value = res.data.list;
  })
}


//商品信息
const productList = ref([])
getProductList();

async function getProductList() {
  let data = {
    pageNum: 1,
    pageSize: 8
  }
  request.get("/product/page", {
    params: data
  }).then(res => {
    productList.value = res.data.list;
  })
}


</script>

<style scoped>
.image-container {
  position: relative; /* 使子元素绝对定位 */
  overflow: hidden; /* 隐藏超出部分 */
}

.image-container img {
  height: 100%;
  object-fit: fill;
  background-size: 100% 100%;
  width: 100%; /* 图片自适应容器 */
  display: block; /* 取消图片底部的空隙 */
}

.overlay {
  position: absolute; /* 绝对定位 */
  bottom: 10px; /* 定位到底部 */
  left: 0; /* 从左边开始 */
  right: 0; /* 右边延伸 */
  background: rgba(0, 0, 0, 0.6); /* 半透明黑色背景 */
  color: white; /* 文字颜色 */
  text-align: center; /* 中心对齐文字 */
  padding: 10px; /* 内边距 */
}

.card-header-more {
  float: right;
  padding: 3px 0
}
</style>
