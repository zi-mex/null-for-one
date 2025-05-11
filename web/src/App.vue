<template>
    <div id="app">
      <el-config-provider :locale="locale" :size="size">
        <router-view/>
      </el-config-provider>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import en from 'element-plus/dist/locale/en.mjs'

const language = ref('zh-cn')
const locale = computed(() => (
    language.value === 'zh-cn' ? zhCn : en))

const size= ref('default')

</script>
<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

html, body, #app {
    height: 100%;
}

.container-shadow {
    box-shadow: 0 0 20px #ccc;
    /*margin:20px 20px;*/
    padding: 20px;
    box-sizing: border-box;
    border-radius: 15px;
}
.el-card{
  --el-card-border-radius:10px
}

</style>
