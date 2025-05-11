<template>
  <div style="width: 60%;margin: 0 auto">
    <el-card>
      <el-space direction="vertical" alignment="left" style="width: 100%">
        <div style="text-align: center">
          <h2 style="font-size: 25px">
            {{info.title}}
          </h2>
        </div>
        <div style="text-align: center">
          <el-space style="color: #f9f9f9;font-size:14px">
            <span>发布时间：{{info.createTime}}</span>
          </el-space>
        </div>
        <div style="padding: 0 15px">
          <div v-html="info.content"></div>
        </div>

      </el-space>

    </el-card>
  </div>
</template>
<script setup>

import {useRoute} from "vue-router";
import {ref} from "vue";
import http from "@/utils/http.js"

const route = useRoute()
const id = ref(route.params.id)
const info = ref({})

getInfo()

function getInfo(){
  http.get("/helpMessage/selectById/"+id.value).then(res => {
    info.value = res.data
  })
}

</script>