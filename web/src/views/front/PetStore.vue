<template>
  <div style="width: 60%;margin: 0 auto">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card shadow="hover">
        <el-space style="flex-wrap: wrap;gap: 15px">
          <el-input v-model="searchForm.storeName"
                    placeholder="请输入店铺名称"
                    style="width: 300px"
                    clearable
          />
          <el-input v-model="searchForm.storeAddress"
                    placeholder="请输入店铺地址"
                    style="width: 300px"
                    clearable
          />
          <el-button type="primary" :icon="Search" @click="search"></el-button>
        </el-space>
      </el-card>
      <el-card shadow="hover"
               v-for="(item,index) in listData"

      >
        <el-descriptions
            class="margin-top"
            :title="item.storeName"
            :column="3"
            border
        >
          <template #extra>
            <el-button type="primary" @click="copy(item)">联系商家</el-button>
          </template>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <user />
                </el-icon>
                店长
              </div>
            </template>
            {{ item.username }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <iphone />
                </el-icon>
                电话
              </div>
            </template>
            {{ item.tel }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <location />
                </el-icon>
                开业时间
              </div>
            </template>
            {{ item.createTime }}
          </el-descriptions-item>

          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <office-building />
                </el-icon>
                地址
              </div>
            </template>
            {{ item.storeAddress }}
          </el-descriptions-item>

        </el-descriptions>
      </el-card>

      <el-card>
        <!--                //分页-->
        <el-pagination
            @current-change="currentChange"
            @size-change="sizeChange"
            :page-size="pageInfo.pageSize"
            :current-page="pageInfo.currentPage"
            background
            layout="total,sizes, prev, pager, next"
            :total="pageInfo.total">
        </el-pagination>
      </el-card>
    </el-space>
  </div>

</template>

<script setup>
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import tools from "@/utils/tools.js";
import {ElMessage} from "element-plus";

const searchFormComponents = ref();
const tableComponents = ref();
const listData = ref([]);
const pageInfo = ref({
  //当前页
  pageNum: 1,
  //分页大小
  pageSize: 10,
  //总条数
  total: 0
});

const searchForm = ref({
  storeName:undefined,
  storeAddress:undefined,
})

const userInfo = tools.getCurrentUser()

getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petStoreManager/page", {
    params: data
  }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

/**
 * 选择分页
 * @param e
 */
function currentChange(e) {
  pageInfo.value.pageNum = e
  getPageList()
}

/**
 * 改变分页数量
 * @param e
 */
function sizeChange(e) {
  pageInfo.value.pageSize = e
  getPageList()
  console.log(e)
}

/**
 * 搜索
 */
function search() {
  pageInfo.value.pageNum = 1
  getPageList()
}


/**
 * 重置搜索框
 */
function resetSearch() {
  searchFormComponents.value.resetFields();
  getPageList()
}

/**
 * 重置
 * @param row
 */
function copy(row){
  navigator.clipboard.writeText(row.tel)
      .then(()=>{
        ElMessage.success("联系电话已复制")
      })
      .catch(err => {
        console.log("复制失败：",err)
      })

}

</script>


<!--<script setup>-->

<!--import {OfficeBuilding, Search} from "@element-plus/icons-vue"-->
<!--import {ref} from "vue";-->

<!--const searchForm = ref({-->
<!--    storeName:undefined,-->
<!--    storeAddress:undefined,-->
<!--})-->
<!--function search(){-->

<!--}-->



