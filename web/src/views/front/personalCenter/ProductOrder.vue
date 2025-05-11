<template>
  <div style="margin-left: 5px">
    <el-tabs v-model="searchForm.status" @tab-change="getPageList">
      <el-tab-pane label="全部" name=""></el-tab-pane>
      <el-tab-pane label="待支付" name="待支付"></el-tab-pane>
      <el-tab-pane label="待发货" name="待发货"></el-tab-pane>
      <el-tab-pane label="待收货" name="待收货"></el-tab-pane>
      <el-tab-pane label="已完成" name="已完成"></el-tab-pane>
      <el-tab-pane label="已取消" name="已取消"></el-tab-pane>
    </el-tabs>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  border>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="productName" label="商品名称"></el-table-column>
          <el-table-column prop="price" label="价格" width="80"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='待支付'" type="warning">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='待发货'" type="info">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='待收货'" type="primary">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='已完成'" type="success">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='已取消'" type="danger">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="nameOfConsignee" label="收货人姓名"></el-table-column>
          <el-table-column prop="telOfConsignee" label="收货人电话"></el-table-column>
          <el-table-column prop="addressOfConsignee" label="收货人地址"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button type="primary" link v-if="scope.row.status==='待支付'" @click="pay(scope.row)">付款</el-button>
              <el-popconfirm title="确认取消订单吗?" @confirm="cancel(scope.row)">
                <template #reference>
                  <el-button link type="danger" v-if="scope.row.status==='待支付'">取消
                  </el-button>
                </template>
              </el-popconfirm>
              <el-popconfirm title="确收收货后钱会打款给卖家，是否继续?" @confirm="confirm(scope.row)">
                <template #reference>
                  <el-button link type="success" v-if="scope.row.status==='待收货'">确认收货</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
          <el-pagination
              @current-change="currentChange"
              @size-change="sizeChange"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.currentPage"
              background
              layout="total,sizes, prev, pager, next"
              :total="pageInfo.total">
          </el-pagination>
        </div>
      </el-card>
    </el-space>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

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
  status: ""
});

getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/productOrder/page", {
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

function pay(row) {
  request.post("/productOrder/pay/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList();
  })
}

function cancel(row) {
  request.post("/productOrder/cancel/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList();
  })
}

function confirm(row) {
  request.post("/productOrder/confirm/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList();
  })
}


</script>

<style scoped>

</style>
