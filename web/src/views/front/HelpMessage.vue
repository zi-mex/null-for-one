<template>
  <div style="width: 60%;margin: 0 auto">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card shadow="hover">
        <el-space style="flex-wrap: wrap; gap: 15px;">
          <el-input
              v-model="searchForm.title"
              placeholder="请输入关键词搜索"
              style="width: 300px;"
              clearable
          />
          <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
          <el-radio-group v-model="searchForm.onlyYou" @change="search">
            <el-radio :label="false">查看全部</el-radio>
            <el-radio :label="true">只看自己</el-radio>
          </el-radio-group>
        </el-space>
        <el-divider></el-divider>
        <!-- 操作按钮 -->
        <el-button type="success" @click="add" :icon="Plus">新增</el-button>
      </el-card>

      <el-card
          v-for="(item,index) in listData"
          shadow="hover"
      >
        <template #header>
          <H3>{{ item.title }}</H3>
          <br>
          <div class="user-details">
            <div class="user-left">
              <el-avatar :src="item.userAvatarUrl" class="user-avatar"></el-avatar>
              <h3 class="username">{{ item.username }}</h3>
            </div>
            <p class="created-at">{{ item.createTime }}</p>
          </div>

        </template>
        <div class="content" :style="{ height: item.showFullContent ? 'auto' : '150px' }">
          <span v-html="item.content"></span>
        </div>
        <template #footer>
          <el-button
              v-if="!item.showFullContent"
              @click="item.showFullContent = true"
              type="info"
              link
          >
            展开
          </el-button>
          <el-button link type="plan" @click="router.push('/helpMessageDetails/'+item.id)">查看详情</el-button>
          <el-button link type="primary" v-if="userInfo.type ==='USER'&& userInfo.id===item.userId"
                     @click="edit(index, item)">编辑
          </el-button>
          <el-button link type="danger" v-if="userInfo.type ==='USER'&& userInfo.id===item.userId"
                     @click="deleteOne(index, item)">删除
          </el-button>
        </template>
      </el-card>
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

        <el-form-item label="标题" prop="title"
                      :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
          <el-input v-model="formData.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <MyEditor :content="formData.content" @content-change="formData.content =$event"
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
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyEditor from "@/components/MyEditor.vue";
import tools from "@/utils/tools.js";
import router from "@/router/index.js";

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
  title: undefined,
  onlyYou: false,
});

const userInfo = tools.getCurrentUser()

getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/helpMessage/page", {
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
      request.post("/helpMessage/add", formData.value).then(res => {
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
      request.put("/helpMessage/update", formData.value).then(res => {
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
    request.delete("/helpMessage/delBatch", {data: ids}).then(res => {
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
  });
}
</script>

<style scoped>

.user-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}

.user-left {
  display: flex;
  align-items: center;
}

.user-avatar {
  margin-right: 10px;
}

.username {
  font-size: 16px;
  font-weight: bold;
  margin: 0;
}

.created-at {
  margin: 0;
  font-size: 14px;
  color: #888;
  white-space: nowrap;
}

.content {
  overflow: hidden;
  transition: height 0.3s;
  padding: 0 16px 16px;
}

</style>
