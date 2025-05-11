<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable filterable style="width: 150px">
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="收货人姓名" prop="nameOfConsignee">
            <el-input v-model="searchForm.nameOfConsignee" clearable></el-input>
          </el-form-item>
          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  border>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="productName" label="商品名称"></el-table-column>
          <el-table-column prop="username" label="用户名称"></el-table-column>
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
          <el-table-column fixed="right" label="高级操作" width="120">
            <template #default="scope">
              <el-button v-if="scope.row.status==='待发货'" type="success" :icon="Position"
                         @click="delivery(scope.row)">发货
              </el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button :icon="Edit" @click="edit(scope.$index, scope.row)">编辑</el-button>
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
    <el-dialog
        v-model="dialogOpen"
        v-if="dialogOpen"
        :title="formData.id?'编辑':'新增'"
        width="500"
    >
      <el-form ref="formRef" :model="formData" label-width="100px">

        <el-form-item label="收货人姓名" prop="nameOfConsignee"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-input v-model="formData.nameOfConsignee"></el-input>
        </el-form-item>
        <el-form-item label="收货人电话" prop="telOfConsignee"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-input v-model="formData.telOfConsignee"></el-input>
        </el-form-item>
        <el-form-item label="收货人地址" prop="addressOfConsignee"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-input v-model="formData.addressOfConsignee"></el-input>
        </el-form-item>


      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit" :icon="Check">提交</el-button>
          <el-button @click="closeDialog" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search, Position} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

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
  username: undefined,
  status: undefined,
  nameOfConsignee: undefined

});

const statusList = ref(['待支付', '待发货', '待收货', '已完成', '已取消'])

const productList = ref([])

getProductList()

function getProductList() {
  request.get("/product/list").then(res => {
    productList.value = res.data;
  })
}

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

const dialogOpen = ref(false);
const formData = ref({});
const formRef = ref();


/**
 * 编辑
 * @param index
 * @param row
 */
function edit(index, row) {
  formData.value = Object.assign({}, row)
  dialogOpen.value = true
}

/**
 * 关闭弹框
 */
function closeDialog() {
  dialogOpen.value = false
}

/**
 * 提交数据
 */
function submit() {
  formRef.value.validate((valid) => {
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查表单!",
        type: 'warning'
      });
      return
    }
    //新增
    if (!formData.value.id) {
      request.post("/productOrder/add", formData.value).then(res => {
        if (!res) {
          return
        }
        dialogOpen.value = false
        ElMessage({
          message: "操作成功",
          type: 'success'
        });
        getPageList()
      })
    } else {
      //更新
      request.put("/productOrder/update", formData.value).then(res => {
        if (!res) {
          return
        }
        dialogOpen.value = false
        ElMessage({
          message: "操作成功",
          type: 'success'
        });
        getPageList()
      })
    }
  })
}

function delivery(row) {
  request.post("/productOrder/delivery/" + row.id).then(res => {
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
