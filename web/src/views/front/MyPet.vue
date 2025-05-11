<template>
  <div style="width: 75%;margin: 0 auto">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <div>
        <el-button type="success" @click="add" :icon="Plus">新增</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="(item,index) in listData">
          <el-card shadow="hover" :style="index>3 ? { marginTop: '10px' } : null">
            <template #header>
              <el-space><H3>{{ item.name }}</H3>
                <el-tag>{{ item.petTypeName }}</el-tag>
              </el-space>
              <p style=" float: right; font-size: 14px;color: #888;">{{ item.createTime }}</p>
            </template>
            <el-image v-if="item.mainImg" style="width: 100%; height: 200px" :src="item.mainImg"></el-image>
            <template #footer>
              <el-button link type="info" @click="feed(item)">上门喂养</el-button>
              <el-button link type="warning" @click="fosterCare(item)">临时寄养</el-button>
              <el-button link type="primary" @click="edit(index, item)">编辑</el-button>
              <el-button link type="danger" @click="deleteOne(index, item)">删除</el-button>
            </template>
          </el-card>

        </el-col>
      </el-row>

      <el-card>
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
    <el-dialog
        v-model="dialogOpen"
        v-if="dialogOpen"
        :title="formData.id?'编辑':'新增'"
        width="800"
    >
      <el-form ref="formRef" :model="formData" label-width="100px">

        <el-form-item label="名称" prop="name"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>
        <el-form-item label="封面图" prop="mainImg"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <MyUpLoad type="imageCard" :limit="1" :files="formData.mainImg" @setFiles="formData.mainImg =$event"
                    v-if="dialogOpen"></MyUpLoad>
        </el-form-item>
        <el-form-item label="类型" prop="petTypeId"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-select v-model="formData.petTypeId" placeholder="请选择" filterable>
            <el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in petTypeList"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="基本信息" prop="basicInfo">
          <MyEditor :content="formData.basicInfo" @content-change="formData.basicInfo =$event"
                    v-if="dialogOpen"></MyEditor>
        </el-form-item>


      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit" :icon="Check">提交</el-button>
          <el-button @click="closeDialog" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>


    <el-dialog
        v-model="feedDialogOpen"
        v-if="feedDialogOpen"
        title="上门喂养"
        width="800"
    >
      <el-form ref="feedFormRef" :model="feedFormData" label-width="100px">

        <el-form-item label="宠物店" prop="petStoreManagerId"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-select v-model="feedFormData.petStoreManagerId" placeholder="请选择" filterable>
            <el-option :label="item.storeName+'  (地址：'+item.storeAddress+')'" :value="item.id" :key="item.id"
                       v-for="item in petStoreManagerList"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="reservedTime"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-date-picker
              v-model="feedFormData.reservedTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择时间"
              :shortcuts="shortcuts"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="5" v-model="feedFormData.remark"></el-input>
        </el-form-item>

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="feedSubmit" :icon="Check">提交</el-button>
          <el-button @click="feedDialogOpen=false" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        v-model="fosterCareDialogOpen"
        v-if="fosterCareDialogOpen"
        title="临时寄养"
        width="800"
    >
      <el-form ref="fosterCareFormRef" :model="fosterCareFormData" label-width="100px">

        <el-form-item label="宠物店" prop="petStoreManagerId"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-select v-model="fosterCareFormData.petStoreManagerId" placeholder="请选择" filterable>
            <el-option :label="item.storeName+'  (地址：'+item.storeAddress+')'" :value="item.id" :key="item.id"
                       v-for="item in petStoreManagerList"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="reservedStartTime"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-date-picker
              v-model="fosterCareFormData.reservedStartTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择时间"
              :shortcuts="shortcuts"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="reservedEndTime"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-date-picker
              v-model="fosterCareFormData.reservedEndTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择时间"
              :shortcuts="shortcuts"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="5" v-model="fosterCareFormData.remark"></el-input>
        </el-form-item>

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="fosterCareSubmit" :icon="Check">提交</el-button>
          <el-button @click="fosterCareDialogOpen=false" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>


  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyEditor from "@/components/MyEditor.vue";
import MyUpLoad from "@/components/MyUpload.vue";
import router from "@/router/index.js";

const listData = ref([]);
const pageInfo = ref({
  //当前页
  pageNum: 1,
  //分页大小
  pageSize: 10,
  //总条数
  total: 0
});
const shortcuts = [
  {
    text: '今天',
    value: new Date(),
  },
  {
    text: '明天',
    value: () => {
      const date = new Date()
      date.setDate(date.getDate() + 1)
      return date
    },
  },
  {
    text: '一周后',
    value: () => {
      const date = new Date()
      date.setDate(date.getDate() + 7)
      return date
    },
  },
]

const petTypeList = ref([])

getPetTypeList()

function getPetTypeList() {
  request.get("/petType/list").then(res => {
    petTypeList.value = res.data;
  })
}

getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = pageInfo.value
  request.get("/pet/page", {
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


const dialogOpen = ref(false);
const formData = ref({});
const formRef = ref();

/**
 * 新增
 */
function add() {
  formData.value = {}
  dialogOpen.value = true
}

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
      request.post("/pet/add", formData.value).then(res => {
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
      request.put("/pet/update", formData.value).then(res => {
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

/**
 * 单个删除
 * @param index
 * @param row
 */
function deleteOne(index, row) {
  batchDelete([row])
}

/**
 * 批量删除
 * @param rows
 */
function batchDelete(rows) {
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/pet/delBatch", {data: ids}).then(res => {
      if (!res) {
        return
      }
      ElMessage({
        message: "操作成功",
        type: 'success'
      });
      getPageList()
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
    tableComponents.value.clearSelection();
  });
}

const petStoreManagerList = ref([])
getPetStoreManagerList()

function getPetStoreManagerList() {
  request.get("/petStoreManager/list").then(res => {
    petStoreManagerList.value = res.data;
  })
}


const feedFormRef = ref()
const feedDialogOpen = ref(false)
const feedFormData = ref({})

function feed(row) {
  feedDialogOpen.value = true
  feedFormData.value = {}
  feedFormData.value.petId = row.id
  feedFormData.value.petTypeId = row.petTypeId
}

function feedSubmit() {
  feedFormRef.value.validate((valid) => {
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查表单!",
        type: 'warning'
      });
      return
    }
    request.post("/petFeed/add", feedFormData.value).then(res => {
      if (!res) {
        return
      }
      feedDialogOpen.value = false
      ElMessage({
        message: "操作成功",
        type: 'success'
      });
      router.push("/personalCenter?type=petFeed")
    })
  })
}


const fosterCareFormRef = ref()
const fosterCareDialogOpen = ref(false)
const fosterCareFormData = ref({})

function fosterCare(row) {
  fosterCareDialogOpen.value = true
  fosterCareFormData.value = {}
  fosterCareFormData.value.petId = row.id
  fosterCareFormData.value.petTypeId = row.petTypeId
}

function fosterCareSubmit() {
  fosterCareFormRef.value.validate((valid) => {
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查表单!",
        type: 'warning'
      });
      return
    }
    request.post("/petFosterCare/add", fosterCareFormData.value).then(res => {
      if (!res) {
        return
      }
      fosterCareDialogOpen.value = false
      ElMessage({
        message: "操作成功",
        type: 'success'
      });
      router.push("/personalCenter?type=petFosterCare")
    })
  })
}


</script>

<style scoped>

</style>
