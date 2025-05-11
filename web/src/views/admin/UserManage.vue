<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="tel">
            <el-input v-model="searchForm.tel" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 150px" filterable>
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button type="" :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>

        <el-space>
          <el-button type="primary" :icon="Plus" @click="add">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">批量删除</el-button>
        </el-space>

      </el-card>
      <el-card>
        <el-table
            :data="listData"
            style="width: 100%"
            border
            tooltip-effect="dark"
            @selection-change="selectionChange"
        >
          <el-table-column type="selection" width="55" />

          <el-table-column property="id" label="ID" width="50" />
          <el-table-column property="username" label="用户名" />
          <el-table-column property="nickname" label="昵称" />
          <el-table-column property="avatarUrl" label="头像" >
            <template #default="scope">
              <el-image
                  v-if="scope.row.avatarUrl"
                  style="width: 100px;height: 100px"
                  :src="scope.row.avatarUrl"
                  :preview-src-list="[scope.row.avatarUrl]"
                  preview-teleported="true"
              >

              </el-image>
            </template>
          </el-table-column>
          <el-table-column property="tel" label="电话号码" />
          <el-table-column property="email" label="邮箱" />
          <el-table-column property="status" label="状态" />
          <el-table-column property="balance" label="余额" />
          <el-table-column property="createTime" label="创建时间" />
          <el-table-column label="高级操作">
            <template #default="scope">
              <el-button type="success" :icon="RefreshLeft" @click="resetPassword(scope.row)">重置密码</el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="" :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button type="danger" :icon="Delete" @click="deleteOne(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div style="margin-top: 20px;">
          <el-pagination
              @current-change="currentChange"
              @size-change="sizeChange"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.pageNum"
              background
              layout="total, sizes, prev, pager, next" :total="pageInfo.total" />
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
        <el-form-item label="用户名" prop="username" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.username" :disabled="formData.id!=null"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.nickname"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatarUrl" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <MyUpload type="imageCard" :limit="1"
                    :files="formData.avatarUrl"
                    @setFiles="formData.avatarUrl = $event"
          ></MyUpload>
        </el-form-item>
        <el-form-item label="电话" prop="tel" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.tel"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.email"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-radio-group v-model="formData.status">
            <el-radio :label="item" :key="item" v-for="item in statusList">{{item}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit" :icon="Check">
            提交
          </el-button>
          <el-button @click="closeDialog" icon="Close">取消</el-button>

        </div>
      </template>
    </el-dialog>

  </div>
</template>
<script setup>

import {ref, toRaw} from "vue";
import {Check, Edit, RefreshLeft, Delete, Refresh, Search, Plus} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/http.js"
import MyUpload from "@/components/MyUpload.vue";


const searchFormComponents=ref()

const searchForm =ref({
  username:undefined,
  tel:undefined,
  status:undefined,
})

//定义状态
const statusList=ref(['启用','禁用'])

const pageInfo=ref({
  //当前分页
  pageNum:1,
  //分页大小
  pageSize:10,
  //总条数
  total:0
})

getPageList()

/**
 * 获取分页数据
 */
function getPageList(){
  //传递参数给后端：分页+模糊查询
  let data = Object.assign(toRaw(searchForm.value),toRaw(pageInfo.value))

  //像后端发起请求
  request.get("/user/page",{
    params:data
  }).then(res=>{
    //获取分页列表中的数据
    listData.value=res.data.list
    //获取数据总数
    pageInfo.value.total=res.data.total
  })
}

/**
 * 选择分页
 * @param e
 */
function currentChange(e){
  pageInfo.value.pageNum=e
  getPageList()
}

/**
 * 改变分页数量
 * @param e
 */
function sizeChange(e){
  pageInfo.value.pageNum=e
  //每页个数改变之后，页数重置为第一页
  pageInfo.value.pageNum=1
  getPageList()
}

const listData =ref([{
  name:"123",
  address:"123456"
}])

function search(){
//将分页重置到第一页
  pageInfo.value.pageNum=1
  //调用分页接口
  getPageList()
}

/**
 * 重置搜索框
 */
function resetSearch(){
  searchFormComponents.value.resetFields()
  getPageList()
}

/**
 * 新增
 */
function add(){
  //清空表单中的数据
  formData.value ={}

  //将弹窗打开
  dialogOpen.value = true
}

/**
 * 编辑
 */
function edit(row){
  //生成一个新的对象
  formData.value = Object.assign({},row)
  dialogOpen.value=true
}

//选中的数据
const selectionRows = ref([])

/**
 * 多选
 * @param rows
 */
function selectionChange(rows){
  selectionRows.value = rows
}

function batchDelete(rows){
  if(!rows){
    rows = selectionRows.value
  }
  //取出每个对象中的id的值
  let ids = rows.map(item=>item.id)
  //弹窗
  ElMessageBox.confirm(
      `此操作将永久删除ID为[${ids}]的数据, 是否继续?`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true,
      }
  )
      .then(() => {
        request.delete("/user/delBatch",{data:ids}).then((res=> {
          if (!res) {
            return
          }
          ElMessage({
            type: 'success',
            message: '操作成功',
          })

          //删除之后，重新获取数据
          getPageList()
        }))
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除',
        })
      })
}

/**
 * 单个删除
 * @param row
 */
function deleteOne(row){
  batchDelete([row])
}

/**
 * 重置密码
 * @param row
 */
function resetPassword(row) {
  request.post("/common/resetPassword?type=USER&id=" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
  })
}

const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()
/**
 * 关闭弹窗
 */
function closeDialog(){
  dialogOpen.value = false
}

/**
 * 提交数据
 */
function submit(){
  formRef.value.validate((valid)=>{
    //校验
    if(!valid){
      ElMessage({
        message:"验证失败，请检查表单",
        type:"warning"
      })
      return
    }
    //新增
    if(!formData.value.id){
      //新增使用post
      request.post("/user/add",formData.value).then(res=>{
        if(!res){
          return
        }
        //关闭弹窗
        dialogOpen.value=false
        ElMessage({
          message:"操作成功",
          type:"success"
        })
        //重新获取列表
        getPageList()
      })
    }else {
      //修改使用put
      request.put("/user/update",formData.value).then(res=>{
        if(!res){
          return
        }
        //关闭弹窗
        dialogOpen.value=false
        ElMessage({
          message:"操作成功",
          type:"success"
        })
        //重新获取列表
        getPageList()
      })
    }

  })
}

</script>