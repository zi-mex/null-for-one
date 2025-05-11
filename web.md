### 2.列表-用户管理模块后端接口开发

##### sql—数据表

```
CREATE TABLE `user` (
`id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
`username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名称',
`password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
`nickname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
`avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
`tel` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
`email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
`status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
`balance` float DEFAULT NULL COMMENT '余额',
`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='普通用户表';
```

##### entity—实体类

```
package com.project.platform.entity;

import java.time.LocalDateTime;

public class User {
/**
* id
*/
private Integer id;
/**
* 用户名称
*/
private String username;
/**
* 密码
*/
private String password;
/**
* 昵称
*/
private String nickname;
/**
* 头像
*/
private String avatarUrl;
/**
* 电话
*/
private String tel;
/**
* 邮箱
*/
private String email;
/**
* 状态
*/
private String status;
/**
* 余额
*/
private Float balance;
/**
* 创建时间
*/
private LocalDateTime createTime;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getNickname() {
return nickname;
}

public void setNickname(String nickname) {
this.nickname = nickname;
}

public String getAvatarUrl() {
return avatarUrl;
}

public void setAvatarUrl(String avatarUrl) {
this.avatarUrl = avatarUrl;
}

public String getTel() {
return tel;
}

public void setTel(String tel) {
this.tel = tel;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public Float getBalance() {
return balance;
}

public void setBalance(Float balance) {
this.balance = balance;
}

public LocalDateTime getCreateTime() {
return createTime;
}

public void setCreateTime(LocalDateTime createTime) {
this.createTime = createTime;
}

}

```

##### mapper—数据访问层

```
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> list();

}
```



##### service—接口

```
public interface UserService extends CommonService{
    List<User> list();
}
```

##### service.impl—实现类

```
@Service //表示在service层，为一个服务
public class UserServiceImpl implements UserService {

@Resource
private UserMapper userMapper;

@Override
public List<User> list() {
return userMapper.list();
}

@Override
public CurrentUserDTO login(String username, String password) {
return null;
}

@Override
public void register(JSONObject data) {

}

@Override
public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {

}

@Override
public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {

}

@Override
public void resetPassword(Integer id) {

}

@Override
public void retrievePassword(RetrievePasswordDTO retrievePasswordDTO) {

}

@Override
public Object selectById(Integer id) {
return null;
}
}

```



##### controller—控制器

```

@RestController  //相当于@Response（将当前类的返回体以json返回）+@Controller(表面当前类是控制器)
@RequestMapping("user")
public class UserController {

@Resource
private UserService userService;

//前端 controller->service->mapper->xml 发起请求
//后端 xml->mapper->service->controller 响应

/**
* 列表
* @return
*/
@GetMapping("list")  //以get方式请求
public ResponseVO<List<User>> list(){
return ResponseVO.ok(userService.list());


}
}

```

### 3.新增-用户管理模块后端接口开发

##### mapper.xml—sql映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.UserMapper">

</mapper>
```

```
<!--
    useGeneratedKeys="true"
    设置允许Mybatis在插入操作后获取该生成的键
    
    keyProperty="id"
    指定Java对象中将存储生成的键的属性，在插入操作完成后，Mybatis会将生成的键值赋值给这个属性
    -->
    <insert id="insert" parameterType="com.project.platform.entity.User"
    useGeneratedKeys="true"
    keyProperty="id"
    >
        INSERT INTO user
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="username!=null">username,</if>
            <if test="password!=null">password,</if>
            <if test="nickname!=null">nickname,</if>
            <if test="avatarUrl!=null">avatar_url,</if>
            <if test="tel!=null">tel,</if>
            <if test="email!=null">email,</if>
            <if test="status!=null">status,</if>
            <if test="balance!=null">balance,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="username!=null">#{username},</if>
            <if test="password!=null">#{password},</if>
            <if test="nickname!=null">#{nickname},</if>
            <if test="avatarUrl!=null">#{avatarUrl},</if>
            <if test="tel!=null">#{tel},</if>
            <if test="email!=null">#{email},</if>
            <if test="status!=null">#{status},</if>
            <if test="balance!=null">#{balance},</if>
        </trim>
    </insert>
```

##### mapper—数据访问层

```
int insert(User entity);

@Select("SELECT * FROM user WHERE username = #{username}")
User selectByUsername(String username);
```

##### service—接口

```
void insert(User entity);
```

##### service.impl—实现类

```
@Override
    public void insert(User entity) {
        //需求：当前用户名不能重复
        check(entity);

        userMapper.insert(entity);
    }

    /**
     * 判断用户是否重复
     * @param entity
     */
    private void check(User entity) {
        User user = userMapper.selectByUsername(entity.getUsername());
        if (user != null && user.getId() != entity.getId()) {
            throw new CustomException("用户名已存在");
        }
    }
```

##### controller—控制器

```
/**
     * 新增
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody User entity){
        userService.insert(entity);
        return ResponseVO.ok();
    }
```

### 4.修改-用户管理模块后端模块接口开发

##### mapper.xml-sql映射文件

```
<update id="updateById" parameterType="com.project.platform.entity.User">
UPDATE user
<set>
<if test="username!=null">
username=#{username},
</if>
<if test="password!=null">
password=#{password},
</if>
<if test="nickname!=null">
nickname=#{nickname},
</if>
<if test="avatarUrl!=null">
avatar_url=#{avatarUrl},
</if>
<if test="tel!=null">
tel=#{tel},
</if>
<if test="email!=null">
email=#{email},
</if>
<if test="status!=null">
status=#{status},
</if>
<if test="balance!=null">
balance=#{balance},
</if>
</set>
WHERE id =# {id}
</update>
```

##### mapper-数据访问层

```
void updateById(User entity);
```

##### service-接口

```
void updateById(User entity);
```

##### service.impl-实现类

```
@Override
public void updateById(User entity) {
check(entity);
userMapper.updateById(entity);
}
```

##### controller-控制器

```
/**
* 更新
* @param entity
* @return
*/
@PutMapping("update")
public ResponseVO update(@RequestBody User entity){
userService.updateById(entity);
return ResponseVO.ok();
}
```

### 5.单选/批量删除-用户管理模块后端接口开发

##### mapper.xml-sql映射文件

```
<delete id="removeByIds">
DELETE FROM user
WHERE id IN
<foreach item="id" collection="list" open="(" separator="," close=")">
#{id}
</foreach>
</delete>
```

##### mapper-数据访问层

```
boolean removeByIds(List<Integer> ids);
```

##### service-接口

```
void removeByIds(List<Integer> ids);
```

##### service.impl-实现类

```
@Override
public void removeByIds(List<Integer> ids) {
userMapper.removeByIds(ids);
}
```

##### controller-控制器

```
/**
* 单选/批量删除
* @param ids
* @return
*/
@DeleteMapping("delBatch")
public ResponseVO delBatch(@RequestBody List<Integer> ids){
userService.removeByIds(ids);
return ResponseVO.ok();
}
```

### 6.分页模糊查询-用户管理模块后端接口开发

##### mapper.xml-sql映射文件

```
<!--分页模糊查询-->
<select id="queryPage" resultType="com.project.platform.entity.User">
SELECT user.*
FROM user
<include refid="queryConditions" />
ORDER BY user.id DESC
LIMIT #{offset} , #{pageSize}
</select>


<!--查询条件-->
<sql id="queryConditions">
<where>
<if test="query.username != null and query.username.trim() != '' ">
AND user.username LIKE CONCAT('%',#{query.username},'%')
</if>
<if test="query.tel != null and query.tel.trim() != '' ">
AND user.tel LIKE CONCAT('%',#{query.tel},'%')
</if>
<if test="query.status != null and query.status .trim() != '' ">
AND user.status  LIKE CONCAT('%',#{query.status },'%')
</if>
</where>
</sql>
<!--查询总数-->
<select id="queryCount" resultType="java.lang.Integer">
SELECT count(user.id) FROM user
<include refid="queryConditions"></include>
</select>
```

##### mapper-数据访问层

```
List<User> queryPage(Integer offset, Integer pageSize,@Param("query") Map<String, Object> query);

int queryCount(@Param("query") Map<String, Object> query);

```

##### service-接口

```
PageVO<User> page(Map<String,Object> query, Integer pageNum, Integer pageSize);
```

##### service.impl-实现类

```
@Override
public PageVO<User> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
PageVO<User> page = new PageVO<>();
//通过查询条件获取列表对象
List<User> list = userMapper.queryPage((pageNum-1)*pageSize,pageSize,query);
page.setList(list);
//通过查询条件获取总数
page.setTotal(userMapper.queryCount((query)));
return page;
}
```

##### controller-控制器

```
/**
* 分页
* @param query 查询对象
* @param pageNum 起始页，默认为1
* @param pageSize 分页大小，默认为10
* @return
*/
@GetMapping("page")
public ResponseVO<PageVO<User>> page(
@RequestParam Map<String ,Object> query,
@RequestParam(defaultValue = "1")Integer pageNum,
@RequestParam(defaultValue = "10")Integer pageSize
){
//调用service中的方法
PageVO<User> page = userService.page(query,pageNum,pageSize);
return ResponseVO.ok(page);
}
```

### 7.通过ID查询&总结

##### mapper-数据访问层

```
@Select("SELECT * FROM user WHERE id = #{id}")
User selectById(Integer id);
```

##### service-接口

```
User selectById(Integer id);
```

##### service.impl-实现类

```
@Override
public User selectById(Integer id) {
return userMapper.selectById(id);
}
```

##### controller-控制器

```
/**
* 通过id查询数据
* @param id
* @return
*/
@GetMapping("selectById/{id}")
public ResponseVO<User> selectById(@PathVariable("id") Integer id){
User user = userService.selectById(id);
return ResponseVO.ok(user);
}
```

### 8.后台用户管理模块界面搭建

##### element-ui plus

[一个 Vue 3 UI 框架 | Element Plus](https://element-plus.org/zh-CN/#/zh-CN)

##### router/index.js

```
{
path: 'user',
name: 'User',
component: () =>
import ('../views/admin/UserManage.vue')
}
```

##### AdminLayout

```
<el-menu-item index="/admin/user">
<el-icon>
<User/>
</el-icon>
<span>普通用户管理</span>
</el-menu-item>
```

### 9.前后端交互完成分页查询功能

##### UserManage.vue

```
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
          <el-button type="danger" :icon="Delete" @click="batchDelete">批量删除</el-button>
        </el-space>

      </el-card>
      <el-card>
        <el-table
            :data="listData"
            style="width: 100%"
            border
            tooltip-effect="dark"
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
              <el-button type="" :icon="Edit" @click="resetPassword(scope.row)">编辑</el-button>
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
        v-model="dialogVisible"
        title="Tips"
        width="500"
        :before-close="handleClose"
    >
      <span>This is a message</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="dialogVisible = false">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-button plain @click="dialogVisible = true">
      Click to open the Dialog
    </el-button>
  </div>
</template>
<script setup>

import {ref, toRaw} from "vue";
import {Edit, RefreshLeft, Delete, Refresh, Search} from "@element-plus/icons-vue";
import {ElMessageBox} from "element-plus";
import request from "@/utils/http.js"


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
  request.get("user/page",{
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

function add(){

}

function batchDelete(){

}

function resetPassword(){

}

function deleteOne(){

}


const dialogVisible = ref(false)

const handleClose = () => {
  ElMessageBox.confirm('Are you sure to close this dialog?')
      .then(() => {
        done()
      })
      .catch(() => {
        // catch error
      })
}

</script>
```

### 10.前后端交互完成新增编辑功能

##### 图片上传

```
<el-form-item label="头像" prop="avatarUrl" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
<MyUpload type="imageCar" :limit="1"
:files="formData.avatarUrl"
@setFiles="formData.avatarUrl = $event"
></MyUpload>
</el-form-item>

import MyUpload from "@/components/MyUpload.vue";
```

##### MyUpload.vue

```
<template>
  <div>
    <el-upload
        ref="upload"
        :action="uploadUrl"
        :list-type="listType"
        :on-preview="handlePreview"
        :file-list="fileList"
        :limit="limit"
        :accept="accept"
        :headers="uploadHeaders"
        :on-success="handleFileSuccess"
        :on-exceed="handleExceed"
        :on-remove="handleRemove">
      <el-button size="small" type="primary"> {{
          limit === 1 && fileList.length > 0 ? '点击替换' : '点击上传'
        }}
      </el-button>
      <div slot="tip" class="el-upload__tip">{{ tip }}</div>
    </el-upload>
    <el-dialog v-model="dialogVisible" v-if="dialogVisible">
      <div>
        <img v-if="type==='image'||type==='imageCard'" width="100%" :src="previewFile.url" alt="">
        <video v-if="type==='video'" width="100%" :src="previewFile.url" controls></video>
        <audio v-if="type==='audio'" width="100%" :src="previewFile.url" controls></audio>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import utils from "@/utils/tools.js";
import {ElMessage, genFileId} from "element-plus";

const props = defineProps({
  /**
   * 文件类型
   */
  type: {
    type: String,
    default: "file"
  },
  /**
   * 文件列表
   */
  files: {
    type: String,
    default: ""
  },
  /**
   * 提示信息
   */
  tip: {
    type: String,
    default: ""
  },
  /**
   * 上传文件数量限制
   */
  limit: {
    type: Number,
    default: 100
  }
});
//组件对象
const upload = ref()
//上传请求头，需要带token
const uploadHeaders = reactive({
  //设置token
  token: utils.getToken()
});
//服务器上传的URL
const uploadUrl = ref('');
//上传文件列表
const fileList = ref([]);
//允许上传的文件类型
const accept = ref('');
//默认文件类型
const listType = ref('picture-card');
//预览弹框
const dialogVisible = ref(false);
//预览文件信息
const previewFile = ref('');

onMounted(() => {
  //设置上传地址
  uploadUrl.value = import.meta.env.VITE_APP_API_URL + "/file/upload";
  load();
});

//加载
function load() {
  if (props.files) {
    //对文件按照，分割
    let files = props.files.split(",");
    for (let file of files) {
      //切割文件名
      let strings = file.split("/");
      fileList.value.push({
        name: strings[strings.length - 1],
        url: file
      });
    }
  }
  switch (props.type) {
      /**
       * 图片卡片类型
       */
    case "imageCard":
      listType.value = "picture-card";
      accept.value = "image/*";
      break;
      /**
       *图片
       */
    case "image":
      listType.value = "picture";
      accept.value = "image/*";
      break;
      /**
       * 视频
       */
    case "video":
      accept.value = "video/*";
      listType.value = "text";
      break;
      /**
       * 音频
       */
    case "audio":
      accept.value = "audio/*";
      listType.value = "text";
      break;
      /**
       * 文件 附件
       */
    case "file":
      listType.value = "text";
      break;
  }
}

//回调父组件，设置文件数据
const emit = defineEmits(['setFiles']);

/**
 * 通知父组件文件改变
 */
function setFiles() {
  let files = fileList.value.map((item) => {
    return item.url;
  });
  emit("setFiles", files.join(","));
}

/**
 * 文件上传成功后的处理
 * @param response
 * @param file
 * @param fileListRes
 */
function handleFileSuccess(response, file, fileListRes) {
  //添加到文件对象
  fileList.value.push({
    name: response.data.name,
    url: response.data.url
  });
  //通知父组件文件改变
  setFiles();
}

/**
 * 删除文件
 * @param file
 * @param fileListRes
 */
function handleRemove(file, fileListRes) {
  fileList.value = fileListRes;
  //通知父组件文件改变
  setFiles();
}

/**
 * 预览文件
 * @param file
 */
function handlePreview(file) {
  //设置预览对象
  previewFile.value = file;
  if (props.type === "file") {
    //文件类型直接下载
    downloadFile();
    return;
  }
  //打开预览弹框
  dialogVisible.value = true;
}

/**
 * 处理文件超出限制的情况
 * @param files
 */
function handleExceed(files) {
  //如果只有一个就进行替换
  if (props.limit === 1) {
    // 这个方法会移除数组的最后一个元素，并返回被移除的元素。如果数组为空，则返回 undefine
    fileList.value.pop()
    upload.value.clearFiles()
    const file = files[0];
    file.uid = genFileId()
    upload.value.handleStart(file)
    upload.value.submit()
  } else {
    ElMessage.warning(`最多只允许上传${props.limit}张图片`);
  }
}

/**
 * 下载文件
 */
function downloadFile() {
  const link = document.createElement('a');
  link.style.display = 'none';
  document.body.appendChild(link);
  link.href = previewFile.value.url;
  console.log(previewFile.value);
  link.setAttribute('download', previewFile.value.name); // 你可以自定义下载时的文件名
  link.click();
  link.remove();
}
</script>


<style>
</style>

```

##### 表单校验

```
<el-form ref="formRef" :model="formData" label-width="100px">
<el-form-item label="用户名" prop="username" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
<el-input v-model="formData.username" :disabled="formData.id!=null"></el-input>
</el-form-item>
</el-form>


const formRef = ref()

formRef.value.validate((valid)=>{
//校验
if(!valid){
ElMessage({
message:"验证失败，请检查表单",
type:"warning"
})
return
}
})
```

### 11.前后端交互完成单选多选删除功能

##### UserManager.vue

```
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
```

### 12.用户模块代码完善

##### 用户登录、用户注册

```
<el-option label="用户" value="USER"></el-option>
```

##### CommonService

```
@Resource
private UserService userService;


private CommonService getCommonService(String type) {
switch (type) {
case "ADMIN":
return adminService;
case "USER":
return userService;
default:
throw new CustomException("用户类型错误");
}
}
```

##### UserServiceeImpl

```
@Value("${resetPassword}")
private String resetPassword;


@Override
public CurrentUserDTO login(String username, String password) {
//查询用户名是否存在
User user = userMapper.selectByUsername(username);
if (user == null||!password.equals(user.getPassword())) {
throw new CustomException("用户名或密码错误");
}

//禁用
if("禁用".equals(user.getStatus())){
throw new CustomException("用户名已禁用");
}

CurrentUserDTO currentUserDTO = new CurrentUserDTO();
BeanUtils.copyProperties(user, currentUserDTO);
return currentUserDTO;
}

@Override
public void register(JSONObject data) {
User user = new User();
user.setUsername(data.getString("username"));
user.setNickname(data.getString("nickname"));
user.setAvatarUrl(data.getString("avatarUrl"));
user.setPassword(data.getString("password"));
//新用户赠送100 余额
user.setBalance(100F);
user.setStatus("启用");
insert(user);
}

@Override
public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {
User user = userMapper.selectById(currentUserDTO.getId());
user.setId(currentUserDTO.getId());
user.setNickname(currentUserDTO.getNickname());
user.setAvatarUrl(currentUserDTO.getAvatarUrl());
user.setTel(currentUserDTO.getTel());
user.setEmail(currentUserDTO.getEmail());
userMapper.updateById(user);
}

@Override
public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {
User user = userMapper.selectById(CurrentUserThreadLocal.getCurrentUser().getId());
if(!user.getPassword().equals(updatePassword.getOldPassword())){
throw new CustomException("旧密码不正确");
}
user.setPassword(updatePassword.getNewPassword());
userMapper.updateById(user);
}

@Override
public void resetPassword(Integer id) {
User user = userMapper.selectById(id);
user.setPassword(resetPassword);
userMapper.updateById(user);
}

```

##### UserManage.vue

```
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
```

### 13.后台宠物店管理模块接口开发+前后端交互

##### sql-数据表

```
CREATE TABLE `pet_store_manager` ( 
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `nickname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `tel` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `store_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺名称',
  `store_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺图片',
  `store_address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='宠物店长';
```

##### entity-实体类

```
public class PetStoreManager  {
   /**
    * id
    */
   private Integer id;
   /**
   * 用户名
   */
   private String username;
   /**
   * 密码
   */
   private String password;
   /**
   * 昵称
   */
   private String nickname;
   /**
   * 头像
   */
   private String avatarUrl;
   /**
   * 电话
   */
   private String tel;
   /**
   * 邮箱
   */
   private String email;
   /**
   * 状态
   */
   private String status;
   /**
   * 店铺名称
   */
   private String storeName;
   /**
    * 店铺图片
    */
   private String storeImg;
   /**
   * 店铺地址
   */
   private String storeAddress;
   /**
   * 创建时间
   */
   private LocalDateTime createTime;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getNickname() {
      return nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }

   public String getAvatarUrl() {
      return avatarUrl;
   }

   public void setAvatarUrl(String avatarUrl) {
      this.avatarUrl = avatarUrl;
   }

   public String getTel() {
      return tel;
   }

   public void setTel(String tel) {
      this.tel = tel;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getStoreName() {
      return storeName;
   }

   public void setStoreName(String storeName) {
      this.storeName = storeName;
   }

   public String getStoreImg() {
      return storeImg;
   }

   public void setStoreImg(String storeImg) {
      this.storeImg = storeImg;
   }

   public String getStoreAddress() {
      return storeAddress;
   }

   public void setStoreAddress(String storeAddress) {
      this.storeAddress = storeAddress;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
```

##### PetStoreManagerMapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.PetStoreManagerMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.PetStoreManager">
        SELECT pet_store_manager.*
        FROM pet_store_manager
        <include refid="queryConditions"/>
        ORDER BY pet_store_manager.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(pet_store_manager.id) FROM pet_store_manager
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
          <if test="query.username != null and query.username.trim() != ''">
            AND pet_store_manager.username LIKE CONCAT('%', #{query.username}, '%')
          </if>
          <if test="query.tel != null and query.tel.trim() != ''">
            AND pet_store_manager.tel LIKE CONCAT('%', #{query.tel}, '%')
          </if>
          <if test="query.status != null and query.status.trim() != ''">
            AND pet_store_manager.status = #{query.status}
          </if>
          <if test="query.storeName != null and query.storeName.trim() != ''">
            AND pet_store_manager.store_name LIKE CONCAT('%', #{query.storeName}, '%')
          </if>
          <if test="query.storeAddress != null and query.storeAddress.trim() != ''">
            AND pet_store_manager.store_address LIKE CONCAT('%', #{query.storeAddress}, '%')
          </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.PetStoreManager" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO pet_store_manager
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="username != null">username,</if>
            <if test="password != null">password,</if>
            <if test="nickname != null">nickname,</if>
            <if test="avatarUrl != null">avatar_url,</if>
            <if test="tel != null">tel,</if>
            <if test="email != null">email,</if>
            <if test="status != null">status,</if>
            <if test="storeName != null">store_name,</if>
            <if test="storeImg != null">store_img,</if>
            <if test="storeAddress != null">store_address,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="username != null">#{username},</if>
            <if test="password != null">#{password},</if>
            <if test="nickname != null">#{nickname},</if>
            <if test="avatarUrl != null">#{avatarUrl},</if>
            <if test="tel != null">#{tel},</if>
            <if test="email != null">#{email},</if>
            <if test="status != null">#{status},</if>
            <if test="storeName != null">#{storeName},</if>
            <if test="storeImg != null">#{storeImg},</if>
            <if test="storeAddress != null">#{storeAddress},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.PetStoreManager">
        UPDATE pet_store_manager
        <set>
            <if test="username != null">
                username = #{username},
            </if>
            <if test="password != null">
                password = #{password},
            </if>
            <if test="nickname != null">
                nickname = #{nickname},
            </if>
            <if test="avatarUrl != null">
                avatar_url = #{avatarUrl},
            </if>
            <if test="tel != null">
                tel = #{tel},
            </if>
            <if test="email != null">
                email = #{email},
            </if>
            <if test="status != null">
                status = #{status},
            </if>
            <if test="storeName != null">
                store_name = #{storeName},
            </if>
            <if test="storeImg != null">
                store_img = #{storeImg},
            </if>
            <if test="storeAddress != null">
                store_address = #{storeAddress},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM pet_store_manager
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
public interface PetStoreManagerMapper {
    List<PetStoreManager> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_store_manager WHERE id = #{id}")
    PetStoreManager selectById(Integer id);

    @Select("SELECT * FROM pet_store_manager")
    List<PetStoreManager> list();

    int insert(PetStoreManager entity);

    int updateById(PetStoreManager entity);

    boolean removeByIds(List<Integer> ids);

    @Select("SELECT * FROM pet_store_manager WHERE username = #{username}")
    PetStoreManager selectByUsername(String username);

    @Select("SELECT * FROM pet_store_manager WHERE store_name = #{storeName}")
    PetStoreManager selectByStoreName(String storeName);
}
```

##### service-接口（业务逻辑层）

```
/**
 * 宠物店长
 */
public interface PetStoreManagerService extends CommonService {

    PageVO<PetStoreManager> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetStoreManager selectById(Integer id);

    List<PetStoreManager> list();

    void insert(PetStoreManager entity);

    void updateById(PetStoreManager entity);

    void removeByIds(List<Integer> id);
}
```

##### service.impl-实现类

```
/**
 * 宠物店长
 */
@Service
public class PetStoreManagerServiceImpl implements PetStoreManagerService {
    @Resource
    private PetStoreManagerMapper petStoreManagerMapper;

    @Value("${resetPassword}")
    private String resetPassword;

    @Override
    public PageVO<PetStoreManager> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetStoreManager> page = new PageVO();
        List<PetStoreManager> list = petStoreManagerMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petStoreManagerMapper.queryCount(query));
        return page;
    }

    @Override
    public PetStoreManager selectById(Integer id) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(id);
        return petStoreManager;
    }

    @Override
    public List<PetStoreManager> list() {
        return petStoreManagerMapper.list();
    }

    @Override
    public void insert(PetStoreManager entity) {
        check(entity);
        if (entity.getPassword() == null) {
            entity.setPassword(resetPassword);
        }
        petStoreManagerMapper.insert(entity);
    }

    @Override
    public void updateById(PetStoreManager entity) {
        check(entity);
        petStoreManagerMapper.updateById(entity);
    }

    private void check(PetStoreManager entity) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectByUsername(entity.getUsername());
        if (petStoreManager != null && petStoreManager.getId() != entity.getId()) {
            throw new CustomException("用户名已存在");
        }
        petStoreManager = petStoreManagerMapper.selectByStoreName(entity.getUsername());
        if (petStoreManager != null && petStoreManager.getId() != entity.getId()) {
            throw new CustomException("店铺名称已存在");
        }
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petStoreManagerMapper.removeByIds(ids);
    }


    @Override
    public CurrentUserDTO login(String username, String password) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectByUsername(username);
        if (petStoreManager == null || !petStoreManager.getPassword().equals(password)) {
            throw new CustomException("用户名或密码错误");
        }
        if (petStoreManager.getStatus().equals("禁用")) {
            throw new CustomException("用户已禁用");
        }
        CurrentUserDTO currentUserDTO = new CurrentUserDTO();
        BeanUtils.copyProperties(petStoreManager, currentUserDTO);
        return currentUserDTO;
    }

    @Override
    public void register(JSONObject data) {
        PetStoreManager petStoreManager = new PetStoreManager();
        petStoreManager.setUsername(data.getString("username"));
        petStoreManager.setNickname(data.getString("nickname"));
        petStoreManager.setAvatarUrl(data.getString("avatarUrl"));
        petStoreManager.setPassword(data.getString("password"));
        petStoreManager.setStoreName(data.getString("storeName"));
        petStoreManager.setStoreImg(data.getString("storeImg"));
        petStoreManager.setStoreAddress(data.getString("storeAddress"));
        petStoreManager.setStatus("启用");
        insert(petStoreManager);
    }


    @Override
    public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(currentUserDTO.getId());
        petStoreManager.setId(currentUserDTO.getId());
        petStoreManager.setNickname(currentUserDTO.getNickname());
        petStoreManager.setAvatarUrl(currentUserDTO.getAvatarUrl());
        petStoreManager.setTel(currentUserDTO.getTel());
        petStoreManager.setEmail(currentUserDTO.getEmail());
        petStoreManagerMapper.updateById(petStoreManager);
    }

    @Override
    public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(CurrentUserThreadLocal.getCurrentUser().getId());
        if (!petStoreManager.getPassword().equals(updatePassword.getOldPassword())) {
            throw new CustomException("旧密码不正确");
        }
        petStoreManager.setPassword(updatePassword.getNewPassword());
        petStoreManagerMapper.updateById(petStoreManager);
    }

    @Override
    public void resetPassword(Integer id) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(id);
        petStoreManager.setPassword(resetPassword);
        petStoreManagerMapper.updateById(petStoreManager);
    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.PetStoreManager;
import com.project.platform.service.PetStoreManagerService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物店长
 */
@RestController
@RequestMapping("/petStoreManager")
public class PetStoreManagerController {
    @Resource
    private PetStoreManagerService petStoreManagerService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetStoreManager>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetStoreManager> page = petStoreManagerService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public  ResponseVO<PetStoreManager> selectById(@PathVariable("id") Integer id) {
        PetStoreManager entity = petStoreManagerService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetStoreManager>> list() {

return ResponseVO.ok(petStoreManagerService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetStoreManager entity) {
        petStoreManagerService.insert(entity);
return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetStoreManager entity) {
        petStoreManagerService.updateById(entity);
return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petStoreManagerService.removeByIds(ids);
return ResponseVO.ok();
    }
}
```

##### router/index.js-路由文件

```
{
  path: 'petStoreManager',
  name: 'admin-petStoreManager',
  component: () =>
      import ('../views/admin/PetStoreManagerManage.vue')
},
```

##### layout/AdminLayout.vue-后台布局文件

```
<el-menu-item index="/admin/admin" v-if="currentUser.type==='ADMIN'">
  <el-icon>
    <User/>
  </el-icon>
  <span>管理员管理</span>
</el-menu-item>

  <el-menu-item index="/admin/user" v-if="currentUser.type==='ADMIN'">
      <el-icon>
          <User/>
      </el-icon>
      <span>普通用户管理</span>
  </el-menu-item>
  <el-menu-item index="/admin/petStoreManager" v-if="currentUser.type==='ADMIN'">
      <el-icon>
          <ShoppingBag/>
      </el-icon>
      <span>宠物店管理</span>
  </el-menu-item>
```

##### PetStoreManagerManage.vue-后台管理页面

```
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
            <el-select v-model="searchForm.status" placeholder="请选择" clearable filterable style="width: 150px">
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="店铺名称" prop="storeName">
            <el-input v-model="searchForm.storeName" clearable></el-input>
          </el-form-item>
          <el-form-item label="店铺地址" prop="storeAddress">
            <el-input v-model="searchForm.storeAddress" clearable></el-input>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" @click="add" :icon="Plus">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">
            批量删除
          </el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  @selection-change="selectionChange"
                  border>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column prop="nickname" label="昵称"></el-table-column>
          <el-table-column prop="avatarUrl" label="头像">
            <template #default="scope">
              <el-image v-if="scope.row.avatarUrl" style="width: 100px; height: 100px" :src="scope.row.avatarUrl"
                        :preview-src-list="[scope.row.avatarUrl]" :preview-teleported="true"></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="tel" label="电话"></el-table-column>
          <el-table-column prop="email" label="邮箱"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column prop="storeName" label="店铺名称"></el-table-column>
          <el-table-column prop="storeImg" label="店铺图片">
            <template #default="scope">
              <el-image v-if="scope.row.storeImg" style="width: 100px; height: 100px" :src="scope.row.storeImg"
                        :preview-src-list="[scope.row.storeImg]" :preview-teleported="true"></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="storeAddress" label="店铺地址"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="高级操作" width="140">
            <template #default="scope">
              <el-button type="success" :icon="RefreshLeft" @click="resetPassword( scope.row)">重置密码</el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <el-button :icon="Edit" @click="edit(scope.$index, scope.row)">编辑</el-button>
              <el-button :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
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

          <el-form-item label="用户名" prop="username"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.username" :disabled="formData.id!=null"></el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickname"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.nickname"></el-input>
          </el-form-item>
          <el-form-item label="头像" prop="avatarUrl"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <MyUpLoad type="imageCard" :limit="1" :files="formData.avatarUrl" @setFiles="formData.avatarUrl =$event"
                      v-if="dialogOpen"></MyUpLoad>
          </el-form-item>
          <el-form-item label="电话" prop="tel">
            <el-input v-model="formData.tel"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="formData.email"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-radio-group v-model="formData.status">
              <el-radio :label="item" :key="item" v-for="item in statusList">{{ item }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="店铺名称" prop="storeName"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.storeName"></el-input>
          </el-form-item>
          <el-form-item label="店铺图片" prop="storeImg"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <MyUpLoad type="imageCard" :limit="1" :files="formData.storeImg" @setFiles="formData.storeImg =$event"
                      v-if="dialogOpen"></MyUpLoad>
          </el-form-item>
          <el-form-item label="店铺地址" prop="storeAddress"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.storeAddress"></el-input>
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
import MyUpLoad from "@/components/MyUpload.vue";

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
  tel: undefined,
  status: undefined,
  storeName: undefined,
  storeAddress: undefined,

});

const statusList = ref(['启用', '禁用'])


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
      request.post("/petStoreManager/add", formData.value).then(res => {
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
      request.put("/petStoreManager/update", formData.value).then(res => {
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

const selectionRows = ref([]);

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
  selectionRows.value = rows
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
  if (!rows) {
    rows = selectionRows.value;
  }
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/petStoreManager/delBatch", {data: ids}).then(res => {
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

/**
 * 重置密码
 * @param row
 */
function resetPassword(row) {
  request.post("/common/resetPassword?type=PET_STORE_MANAGER&id=" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
  })
}
</script>

<style scoped>

</style>
```

### 14.登录注册界面、修改用户信息模块新增宠物店角色

##### CommonController

```
@Resource
private PetStoreManagerService petStoreManagerService;


    /**
     * 根据类型获取对应service
     *
     * @param type
     * @return
     */

    private CommonService getCommonService(String type) {
        switch (type) {
            case "ADMIN":
                return adminService;
            case "USER":
                return userService;
            case "PET_STORE_MANAGER":
                return petStoreManagerService;
            default:
                throw new CustomException("用户类型错误");
        }
    }
```

##### Login.vue

```
<el-option label="宠物店长" value="PET_STORE_MANAGER"></el-option>
```

##### Register.vue

```
<template>
  <div class="main-context">
    <el-card class="box-card">
      <el-space direction="vertical" style="width: 100%" size="large">
        <el-space>
<!--          <img src="../assets/logo.png" width="100%" style="width: 55px">-->
          <el-space direction="vertical" style="width: 100%" size="small">
              <h2 style="font-style: oblique">宠物综合服务平台</h2>
              <!--            <span style="font-style: oblique;font-size: 15px">javadh.com</span>-->
          </el-space>
        </el-space>
        <el-form :model="formData" label-width="80px" :rules="rules" ref="formRef" style="width: 100%" >


            <el-row>
                <el-col :span="12">
                    <el-form-item label="头像" prop="avatarUrl"
                                  style="width: 100%"
                                  :rules="[{required:true,message:'请选择头像',trigger:[ 'blur','change']}]">
                        <MyUpLoad type="imageCard" :limit="1" :files="formData.avatarUrl"

                                  @setFiles="formData.avatarUrl =$event"></MyUpLoad>
                    </el-form-item>
                    <el-form-item label="用户类型" prop="type"
                                  :rules="[{required:true,message:'请选择类型',trigger:[ 'blur','change']}]">
                        <el-select v-model="formData.type" placeholder="请选择用户类型" style="width: 180px">
                            <!--                注意：管理员不能够注册-->
                            <el-option label="普通用户" value="USER"></el-option>
                            <el-option label="宠物店长" value="PET_STORE_MANAGER"></el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="用户名" prop="username"
                                  :rules="[{required:true,message:'请输入用户名',trigger:[ 'blur','change']}]">
                        <el-input
                            style="width: 180px"
                            placeholder="请输入用户名"
                            v-model.trim="formData.username"
                            clearable
                        >
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="店铺图片" prop="storeImg"
                                  v-if="formData.type==='PET_STORE_MANAGER'"
                                  :rules="[{required:true,message:'请选择图片',trigger:['blur','change']}]"
                                  style="width: 100%">
                        <MyUpLoad type="imageCard" :limit="1" :files="formData.storeImg"
                                  @setFiles="formData.storeImg=$event">
                        </MyUpLoad>
                    </el-form-item>
                    <el-form-item label="密码" prop="password"
                                  :rules="[{required:true,message:'请输入密码',trigger:[ 'blur','change']}]">
                        <el-input
                            style="width: 180px"
                            placeholder="请输入密码"
                            show-password
                            v-model.trim="formData.password"
                            clearable
                        >
                        </el-input>
                    </el-form-item>
                    <el-form-item label="昵称" prop="nickname"
                                  :rules="[{required:true,message:'请输入昵称',trigger:[ 'blur','change']}]">
                        <el-input
                            style="width: 180px"
                            placeholder="请输入昵称"
                            v-model.trim="formData.nickname"
                            clearable
                        >
                        </el-input>
                    </el-form-item>
                    <!--            v-if 是否显示
                    :rules 规则
                    -->
                    <el-form-item label="店铺名称" prop="storeName"
                                  v-if="formData.type==='PET_STORE_MANAGER'"
                                  :rules="[{required:true,message:'请输入店铺名称',trigger:['blur','change']}]">
                        <el-input style="width: 180px"
                                  placeholder="请输入店铺名称"
                                  v-model.trim="formData.storeName"
                                  clearable>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="店铺地址" prop="storeAddress"
                                  v-if="formData.type==='PET_STORE_MANAGER'"
                                  :rules="[{required:true,message:'请输入店铺地址',trigger:['blur','change']}]">
                        <el-input style="width: 180px"
                                  placeholder="请输入店铺地址"
                                  v-model.trim="formData.storeAddress"
                                  clearable>
                        </el-input>
                    </el-form-item>

                </el-col>
            </el-row>



<!--            /**-->
<!--            * 店铺名称-->
<!--            */-->
<!--            private String storeName;-->
<!--            /**-->
<!--            * 店铺图片-->
<!--            */-->
<!--            private String storeImg;-->
<!--            /**-->
<!--            * 店铺地址-->
<!--            */-->
<!--            private String storeAddress;-->

          <el-form-item label="" style="width: 100%">
            <el-space direction="vertical" alignment="left" style="width: 100%">
              <el-button @click="submitForm()" type="success" style="width: 100%">注 册</el-button>
              <router-link tag="span" :to="{path:'login'}">
                <el-button type="text" class="button" style="float: right">已有账号？去登录</el-button>
              </router-link>
            </el-space>

          </el-form-item>
        </el-form>
      </el-space>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";
import MyUpLoad from "@/components/MyUpload.vue";

const formRef = ref(null);
const formData = ref({
  type: 'USER',
  username: '',
  nickname: '',
  avatarUrl: '',
  password: ''
});

const rules = ref({
  username: [
    {required: true, message: '请输入用户名称', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
});

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      return
    }
    http.put("/common/register", formData.value).then(res => {
      if (!res) {
        return
      }
      ElMessage({
        message: "注册成功，正在跳转",
        type: "success"
      });
      window.location.href = "/login";
    });
  });
}

</script>

<style scoped>
.main-context {
  height: 100vh; /* 使 .app 高度为视口高度 */
  background: url("../assets/login.png") no-repeat center center fixed;
  background-size: cover; /* 使用 cover 保持图片比例 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: white; /* 根据背景图片调整文字颜色 */
}

.box-card {
  width: 700px;
  margin: 0 auto;
  text-align: center;
}
</style>
```

##### EditCurrentUser.vue

```
      <el-form :model="formData" label-width="100px">
          <el-form-item label="店铺名称" prop="storeName"
                        v-if="formData.type==='PET_STORE_MANAGER'"
                        :rules="[{required:true,message:'请输入店铺名称',trigger:['blur','change']}]">
              <el-input
                        placeholder="请输入店铺名称"
                        v-model.trim="formData.storeName"
                        clearable>
              </el-input>
          </el-form-item>
          <el-form-item label="店铺地址" prop="storeAddress"
                        v-if="formData.type==='PET_STORE_MANAGER'"
                        :rules="[{required:true,message:'请输入店铺地址',trigger:['blur','change']}]">
              <el-input
                        placeholder="请输入店铺地址"
                        v-model.trim="formData.storeAddress"
                        clearable>
              </el-input>
          </el-form-item>
          <el-form-item label="店铺图片" prop="storeImg"
                        v-if="formData.type==='PET_STORE_MANAGER'"
                        :rules="[{required:true,message:'请选择图片',trigger:['blur','change']}]"
                        style="width: 100%">
              <MyUpLoad type="imageCard" :limit="1" :files="formData.storeImg"
                        @setFiles="formData.storeImg=$event">
              </MyUpLoad>
          </el-form-item>
```

##### CurrentUserDTO-数据转换对象

```
    /**
     * 余额
     */
    private Float balance;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺图片
     */
    private String storeImg;
    /**
     * 店铺地址
     */
    private String storeAddress;
```

### 15.后台求助信息管理模块接口开发

##### sql-数据表

```
CREATE TABLE `help_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `user_id` int(11) NOT NULL COMMENT '用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求助信息表';
```

##### entity-实体类

```
package com.project.platform.entity;

import java.time.LocalDateTime;

/**
 * 求助信息
 */
public class HelpMessage {

    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private LocalDateTime createTime;

    private String username;
    private String userAvatarUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }
}
```

##### mapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.HelpMessageMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.HelpMessage">
        SELECT help_message.*
             ,user.username AS username
             ,user.avatar_url AS userAvatarUrl
        FROM help_message
        LEFT JOIN user ON help_message.user_id=user.id
        <include refid="queryConditions"/>
        ORDER BY help_message.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(help_message.id) FROM help_message
        LEFT JOIN user ON help_message.user_id=user.id
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
            <if test="query.title != null and query.title.trim() != ''">
                AND help_message.title LIKE CONCAT('%', #{query.title}, '%')
            </if>
            <if test="query.userId != null">
                AND help_message.user_id = #{query.userId}
            </if>
            <if test="query.username != null and query.username.trim() != ''">
                AND user.username LIKE CONCAT('%', #{query.username}, '%')
            </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.HelpMessage" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO help_message
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="title != null">title,</if>
            <if test="content != null">content,</if>
            <if test="userId != null">user_id,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="title != null">#{title},</if>
            <if test="content != null">#{content},</if>
            <if test="userId != null">#{userId},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.HelpMessage">
        UPDATE help_message
        <set>
            <if test="title != null">
             title = #{title},
            </if>
            <if test="content != null">
             content = #{content},
            </if>
            <if test="userId != null">
             user_id = #{userId},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM help_message
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
package com.project.platform.mapper;

import com.project.platform.entity.HelpMessage;
import com.project.platform.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface HelpMessageMapper {

    @Select("SELECT * FROM help_message")
    List<HelpMessage> list();

    int insert(HelpMessage entity);

    void updateById(HelpMessage entity);

    boolean removeByIds(List<Integer> ids);

    List<HelpMessage> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String ,Object> query);

    int queryCount(@Param("query") Map<String,Object> query);

    @Select("SELECT * FROM help_message WHERE id = #{id}")
    HelpMessage selectById(Integer id);
}
```

##### service-接口（业务逻辑层）

```
package com.project.platform.service;

import com.project.platform.entity.HelpMessage;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 求助信息
 */
public interface HelpMessageService {

    PageVO<HelpMessage> page(Map<String,Object> query,Integer pageNum,Integer pageSize);

    HelpMessage selectById(Integer id);

    List<HelpMessage> list();

    void insert(HelpMessage entity);

    void updateById(HelpMessage entity);

    void removeByIds(List<Integer> ids);


}
```

##### service.impl-实现类

```
package com.project.platform.service.impl;

import com.project.platform.entity.HelpMessage;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.HelpMessageMapper;
import com.project.platform.service.HelpMessageService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HelpMessageServiceImpl implements HelpMessageService {

    @Resource
    private HelpMessageMapper helpMessageMapper;

    @Override
    public PageVO<HelpMessage> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<HelpMessage> page = new PageVO<>();
        //判断是看自己还是看所有人
        if(query.containsKey("onlyYou")&&Boolean.valueOf(query.get("onlyYou").toString())){
            Integer userId =-1;
            if(CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
                //普通用户看自己的
                userId = CurrentUserThreadLocal.getCurrentUser().getId();
            }
            query.put("userId",userId);
        }
        //执行分页查询操作
        List<HelpMessage> list = helpMessageMapper.queryPage((pageNum - 1) * pageSize,pageSize,query);
        page.setList(list);
        //获取总条数
        page.setTotal(helpMessageMapper.queryCount(query));
        return page;
    }

    @Override
    public HelpMessage selectById(Integer id) {
        HelpMessage helpMessage = helpMessageMapper.selectById(id);
        return helpMessage;
    }

    @Override
    public List<HelpMessage> list() {
        return helpMessageMapper.list();
    }

    @Override
    public void insert(HelpMessage entity) {
        //求助模块一般由用户进行操作，管理员只负责管理
        if(!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            throw new CustomException("普通用户才允许添加");
        }

        //插入的时候如果有userId，就要在这里进行操作
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        helpMessageMapper.insert(entity);
    }

    @Override
    public void updateById(HelpMessage entity) {
        check(entity);
        helpMessageMapper.updateById(entity);
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        helpMessageMapper.removeByIds(ids);
    }

    public void check(HelpMessage entity){

    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.HelpMessage;
import com.project.platform.service.HelpMessageService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 求助信息
 */
@RestController
@RequestMapping("/helpMessage")
public class HelpMessageController {

    @Resource
    private HelpMessageService helpMessageService;

    /**
     * 分页查询
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<HelpMessage>> page(
            @RequestParam Map<String,Object> query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize

    ){
        PageVO<HelpMessage> page = helpMessageService.page(query,pageNum,pageSize);
        return ResponseVO.ok(page);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<HelpMessage> selectById(@PathVariable("id") Integer id){
        HelpMessage entity = helpMessageService.selectById(id);
        return ResponseVO.ok(entity);
    }

    /**
     * 列表
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<HelpMessage>> list(){
        return ResponseVO.ok(helpMessageService.list());
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody HelpMessage entity){
        helpMessageService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody HelpMessage entity){
        helpMessageService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids){
        helpMessageService.removeByIds(ids);
        return ResponseVO.ok();
    }

}
```

### 16.后台求助信息管理模块+前后端交互

##### router/index.js-路由文件

```
{
path:'helpMessage',
name:'admin-helpMessage',
component: ()=>
import('../views/admin/HelpMessageManage.vue')
}
```

##### layout/AdminLayout.vue-后台布局文件

```
<el-menu-item index="/admin/helpMessage" v-if="currentUser.type==='ADMIN'">
    <el-icon>
        <help />
    </el-icon>
    求助信息管理
</el-menu-item>
```

##### HelpMessageManage.vue-后台页面

```
<template>
    <div>
        <el-space direction="vertical" alignment="left" style="width: 100%">
            <el-card>
                <el-form ref="searchFormComponents" :model="searchForm" inline>
                    <el-form-item label="标题" prop="title">
                        <el-input v-model="searchForm.title" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="名称" prop="username">
                        <el-input v-model="searchForm.username" clearable></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
                        <el-button type="" :icon="Refresh" @click="resetSearch">重置</el-button>
                    </el-form-item>
                </el-form>

                <el-space>
                    <el-button type="primary" :icon="Plus" @click="add">新增</el-button>
                    <el-button type="danger" :icon="Delete" @click="batchDelete(null)"
                               :disabled="selectionRows.length<=0">批量删除
                    </el-button>
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
                    <el-table-column type="selection" width="55"/>
                    <el-table-column property="id" label="ID" width="50"/>
                    <el-table-column property="title" label="标题"/>
                    <el-table-column property="username" label="用户名称"/>
                    <el-table-column property="createTime" label="创建时间"/>
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
                        layout="total,sizes,prev, pager, next" :total="pageInfo.total"/>
                </div>

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
                              :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
                    <el-input v-model="formData.title" :disabled="formData.id!=null"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content"
                              :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">

<!--                    <el-input v-model="formData.content"></el-input>-->

                    <MyEditor :content="formData.content" @content-change="formData.content=$event"
                              v-if="dialogOpen"
                    ></MyEditor>

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

import request from "@/utils/http.js"
import {Check, Edit, RefreshLeft, Plus, Delete, Search, Refresh} from '@element-plus/icons-vue'

import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyEditor from "@/components/MyEditor.vue"

const searchFormComponents = ref()

const searchForm = ref({
    title: undefined,
    username: undefined,
})


const pageInfo = ref({
    //当前页
    pageNum: 1,
    //分页大小
    pageSize: 10,
    //总条数
    total: 0
})

getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
    //要传递哪些参数给后端：分页+模糊查询 思考？
    let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))

    //向后端去发起请求
    request.get("/helpMessage/page", {
        params: data
    }).then(res => {
        //获取分页列表中的数据
        listData.value = res.data.list
        //获取数据总数
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
 */
function sizeChange(e) {
    pageInfo.value.pageSize = e
    //每页个数改变之后，页数重置为第一页
    pageInfo.value.pageNum = 1
    getPageList()
}

const listData = ref([{
    name: "123",
    address: "123456"
}])

function search() {
//将分页重置到第一页
    pageInfo.value.pageNum = 1
    // 调用分页接口
    getPageList()
}

/**
 * 重置搜索框
 */
function resetSearch() {
    searchFormComponents.value.resetFields()
    getPageList()
}

/**
 * 新增
 */
function add() {
    //清空表单中的数据
    formData.value = {}

    //将弹窗打开
    dialogOpen.value = true
}

/**
 * 编辑
 */
function edit(row) {
    //生成一个新的对象
    formData.value = Object.assign({}, row)
    dialogOpen.value = true
}

//选中的数据
const selectionRows = ref([])

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
    selectionRows.value = rows
}

function batchDelete(rows) {
    if (!rows) {
        rows = selectionRows.value
    }
    //取出每个对象中的id的值
    let ids = rows.map(item => item.id)
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
            request.delete("/helpMessage/delBatch", {data: ids}).then((res => {
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
function deleteOne(row) {
    batchDelete([row])
}



const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()

/**
 * 关闭弹窗
 */
function closeDialog() {
    dialogOpen.value = false
}

/**
 * 提交数据
 */
function submit() {
    formRef.value.validate((valid) => {
        //校验
        if (!valid) {
            ElMessage({
                message: "验证失败，请检查表单",
                type: "warning"
            })
            return
        }
        //新增
        if (!formData.value.id) {
            //新增使用post
            request.post("/helpMessage/add", formData.value).then(res => {
                if (!res) {
                    return
                }
                //关闭弹窗
                dialogOpen.value = false
                ElMessage({
                    message: "操作成功",
                    type: "success"
                })
                //重新获取列表
                getPageList()
            })
        } else {
            //修改使用put
            request.put("/helpMessage/update", formData.value).then(res => {
                if (!res) {
                    return
                }
                //关闭弹窗
                dialogOpen.value = false
                ElMessage({
                    message: "操作成功",
                    type: "success"
                })
                //重新获取列表
                getPageList()
            })
        }

    })
}

</script>
```

### 17.前端模块搭建

##### router/index.js-路由文件【defaultRoutes】

```
        {
            path:'/',
            name:'front',
            component: FrontLayout,
            redirect: '/index',
            children: [{
                path:'index',
                name:'index',
                component: () =>
                    import('../views/front/Index.vue')
            },
            {
                path:'editCurrentUser',
                name:'editCurrentUser',
                component: () =>
                    import('../views/EditCurrentUser.vue')
            },
            {
                path:'editPassword',
                name:'editPassword',
                component: () =>
                    import('../views/EditPassword.vue')
            },
            ]
        },
```

##### layout/FrontLayout.vue-前台布局文件

```
<template>
  <el-container class="my-container">
    <el-header class="my-header">
      <el-row class="nav">
        <el-col :span="4" class="logo">
          <div style="text-align: center;padding-top: 10px" @click="router.push('/')">
            <h1>管理系统</h1>
          </div>
        </el-col>
        <el-col :span="16">
          <div style="text-align: center">
            <el-menu
                :default-active="useRoute().path"
                mode="horizontal"
                router
                @select="handleSelect"
            >
              <el-menu-item index="/index">首页</el-menu-item>
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
                    <span style="font-size: 16px">  {{ currentUser.username }}</span></el-space>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item><span @click="editCurrentUser">个人信息</span></el-dropdown-item>
                    <el-dropdown-item><span @click="editPassword">修改密码</span></el-dropdown-item>
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
      <p>版权所有，翻版必究</p>
      <p>©2025 B站：程序员瑞哥 </p>
    </el-footer>
  </el-container>
</template>

<script setup>
import tools from "@/utils/tools.js";
import {ref} from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {useRoute} from "vue-router";

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

</script>


<style scoped>
.my-container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 例如，设置为视窗的高度 */
}

.my-header {

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
```

##### front/Index.vue

```
<template>
  <div style="width: 75%;margin: 0 auto">
   什么都没有


  </div>
</template>
<script setup>

</script>
```

##### Login.vue

```
if (currentUser.type !== "USER") {
  router.push({path:"/admin"})
} else {
  router.push({path:"/"})
}
```

### 18.前台救助信息模块开发

##### router/index.js

```
{
    path:'helpMessage',
    name:'front-helpMessage',
    component: () =>
        import('../views/front/HelpMessage.vue')
},
```

##### layout/FrontLayout.vue

```
<el-menu-item index="/helpMessage">求助信息</el-menu-item>
```

##### HelpMessage.vue

```
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
```

##### router/index.js

```
{
    path:'helpMessageDetails/:id',
    name:'front-helpMessageDetails',
    component: () =>
        import('../views/front/HelpMessageDetails.vue')
},
```

##### HelpMessageDetails.vue

```
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
                    <el-space style="color: #999999;font-size:14px">
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
```

### 19.前台查找店铺模块开发

##### router/index.js-路由文件

```
{
    path:'petStore',
    name:'front-petStore',
    component: () =>
        import("../views/front/PetStore.vue")
}
```

##### layout/FrontLayout.vue-前台布局文件

```
<el-menu-item index="/petStore">查找店铺</el-menu-item>
```

##### PetStore.vue-前台文件

```
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
```

##### EditCurrentUser.vue

```
<template>
  <div style="width:600px;margin: 0 auto">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人信息修改</span>
        </div>
      </template>
      <el-form :model="formData" label-width="100px">
        <el-form-item prop="img" label="头像">
          <MyUpLoad type="imageCard" :limit="1" :files="formData.avatarUrl"
                    @setFiles="formData.avatarUrl =$event"></MyUpLoad>
        </el-form-item>
        <el-form-item prop="username" label="用户名">
          <el-input type="text"
                    v-model="formData.username"
                    auto-complete="off"
                    placeholder="用户名"
                    :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item prop="nickname" label="昵称">
          <el-input type="text"
                    v-model="formData.nickname"
                    auto-complete="off"
                    placeholder="用户名"
                    :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input
              v-model="formData.email"
              auto-complete="off"
              placeholder="邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item prop="tel" label="电话">
          <el-input type="text"
                    v-model="formData.tel"
                    auto-complete="off"
                    placeholder="电话"
          ></el-input>
        </el-form-item>
          <el-form-item label="店铺名称" prop="storeName"
                        v-if="formData.type === 'PET_STORE_MANAGER'">
              <el-input
                  placeholder="请输入店铺名称"
                  v-model.trim="formData.storeName"
                  clearable
              >
              </el-input>
          </el-form-item>
          <el-form-item label="店铺地址" prop="storeAddress"
                        v-if="formData.type === 'PET_STORE_MANAGER'">
              <el-input
                  placeholder="请输入店铺地址"
                  v-model.trim="formData.storeAddress"
                  clearable
              >
              </el-input>
          </el-form-item>
          <el-form-item label="店铺图片" prop="storeImg"
                        v-if="formData.type === 'PET_STORE_MANAGER'">
              <MyUpLoad type="imageCard" :limit="1" :files="formData.storeImg"

                        @setFiles="formData.storeImg =$event"></MyUpLoad>
          </el-form-item>

        <el-form-item style="width:100%;">
          <el-button type="primary" style="width:100px;" @click="handleSubmit">修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import utils from "@/utils/tools.js";
import MyUpLoad from "@/components/MyUpload.vue";
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";

const formData = ref({});

load();

function load() {
  formData.value = utils.getCurrentUser();
}


function handleSubmit() {
  http.post('/common/updateCurrentUser', formData.value).then(res => {
    http.get("/common/currentUser").then(res1 => {
      let currentUser = res1.data;
      localStorage.setItem("currentUser", JSON.stringify(currentUser));
      ElMessage({
        message: '修改成功',
        type: 'success'
      });
    })
  });
};
</script>
```

### 20.宠物管理-后台宠物类型模块接口开发

##### sql-数据表

```
CREATE TABLE `pet_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='宠物类型';
```

##### entity-实体类

```
package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 宠物类型
 */
@Data
public class PetType  {
   /**
    * id
    */
   private Integer id;
   /**
   * 名称
   */
   private String name;
   /**
   * 备注
   */
   private String remark;
   /**
   * 创建时间
   */
   private LocalDateTime createTime;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getRemark() {
      return remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
```

##### mapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.PetTypeMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.PetType">
        SELECT pet_type.*
        FROM pet_type
        <include refid="queryConditions"/>
        ORDER BY pet_type.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(pet_type.id) FROM pet_type
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
          <if test="query.name != null and query.name.trim() != ''">
            AND pet_type.name LIKE CONCAT('%', #{query.name}, '%')
          </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.PetType" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO pet_type
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="name != null">name,</if>
            <if test="remark != null">remark,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="name != null">#{name},</if>
            <if test="remark != null">#{remark},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.PetType">
        UPDATE pet_type
        <set>
            <if test="name != null">
             name = #{name},
            </if>
            <if test="remark != null">
             remark = #{remark},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM pet_type
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
package com.project.platform.mapper;

import com.project.platform.entity.PetType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetTypeMapper {
    List<PetType> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_type WHERE id = #{id}")
    PetType selectById(Integer id);

    @Select("SELECT * FROM pet_type")
    List<PetType> list();

    int insert(PetType entity);

    int updateById(PetType entity);

    boolean removeByIds(List<Integer> ids);

}
```

##### service-接口（业务逻辑层）

```
package com.project.platform.service;

import com.project.platform.entity.PetType;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物类型
 */
public interface PetTypeService {

    PageVO<PetType> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetType selectById(Integer id);

    List<PetType> list();

    void insert(PetType entity);

    void updateById(PetType entity);

    void removeByIds(List<Integer> id);
}
```

##### service.impl-实现类

```
package com.project.platform.service.impl;

import com.project.platform.entity.PetType;
import com.project.platform.mapper.PetTypeMapper;
import com.project.platform.service.PetTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物类型
 */
@Service
public class PetTypeServiceImpl  implements PetTypeService {
    @Resource
    private PetTypeMapper petTypeMapper;
    
    @Override
    public PageVO<PetType> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetType> page = new PageVO();
        List<PetType> list = petTypeMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petTypeMapper.queryCount(query));
        return page;
    }

    @Override
    public PetType selectById(Integer id) {
        PetType petType = petTypeMapper.selectById(id);
        return petType;
    }

    @Override
    public List<PetType> list() {
        return petTypeMapper.list();
    }
    @Override
    public void insert(PetType entity) {
        check(entity);
        petTypeMapper.insert(entity);
    }
    @Override
    public void updateById(PetType entity) {
        check(entity);
        petTypeMapper.updateById(entity);
    }
    private void check(PetType entity) {

    }
    @Override
    public void removeByIds(List<Integer> ids) {
        petTypeMapper.removeByIds(ids);
    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.PetType;
import com.project.platform.service.PetTypeService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物类型
 */
@RestController
@RequestMapping("/petType")
public class PetTypeController {
    @Resource
    private PetTypeService petTypeService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetType>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetType> page = petTypeService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<PetType> selectById(@PathVariable("id") Integer id) {
        PetType entity = petTypeService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetType>> list() {
        return ResponseVO.ok(petTypeService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetType entity) {
        petTypeService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetType entity) {
        petTypeService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petTypeService.removeByIds(ids);
        return ResponseVO.ok();
    }
}
```

### 21.宠物管理-后台宠物类型模块+前后端交互

##### router/index.js-路由文件

```
{
    path:'petType',
    name:'admin-petType',
    component: () =>
        import("../views/admin/PetTypeManage.vue")
}
```

##### layout/AdminLayout.vue-后台布局文件

```
<el-sub-menu index="1" v-if="currentUser.type==='ADMIN'">
    <template #title>
        <el-icon>
            <shopping-bag />
        </el-icon>
        <span>宠物管理</span>
    </template>
    <el-menu-item index="/admin/petType">
        <el-icon>
            <folder />
        </el-icon>
        <span>宠物类型</span>
    </el-menu-item>
</el-sub-menu>
```

##### PetTypeManage.vue-后台文件

```
<template>
    <div>
        <el-space direction="vertical" alignment="left" style="width: 100%">
            <el-card>
                <el-form ref="searchFormComponents" :model="searchForm" inline>
                    <el-form-item label="名称" prop="name">
                        <el-input v-model="searchForm.name" clearable></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
                        <el-button type="" :icon="Refresh" @click="resetSearch">重置</el-button>
                    </el-form-item>
                </el-form>

                <el-space>
                    <el-button type="primary" :icon="Plus" @click="add">新增</el-button>
                    <el-button type="danger" :icon="Delete" @click="batchDelete(null)"
                               :disabled="selectionRows.length<=0">批量删除
                    </el-button>
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
                    <el-table-column type="selection" width="55"/>
                    <el-table-column property="id" label="ID" width="50"/>
                    <el-table-column property="name" label="名称"/>
                    <el-table-column property="remark" label="备注"/>
                    <el-table-column property="createTime" label="创建时间"/>
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
                        layout="total,sizes,prev, pager, next" :total="pageInfo.total"/>
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
                <el-form-item label="名称" prop="name"
                              :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
                    <el-input v-model="formData.name" ></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark"
                              :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
                    <el-input v-model="formData.remark" ></el-input>
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

import request from "@/utils/http.js"
import {Check, Edit, RefreshLeft, Plus, Delete, Search, Refresh} from '@element-plus/icons-vue'

import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyEditor from "@/components/MyEditor.vue"

const searchFormComponents = ref()

const searchForm = ref({
    name: undefined,
})


const pageInfo = ref({
    //当前页
    pageNum: 1,
    //分页大小
    pageSize: 10,
    //总条数
    total: 0
})

getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
    //要传递哪些参数给后端：分页+模糊查询 思考？
    let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))

    //向后端去发起请求
    request.get("/petType/page", {
        params: data
    }).then(res => {
        //获取分页列表中的数据
        listData.value = res.data.list
        //获取数据总数
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
 */
function sizeChange(e) {
    pageInfo.value.pageSize = e
    //每页个数改变之后，页数重置为第一页
    pageInfo.value.pageNum = 1
    getPageList()
}

const listData = ref([{
    name: "123",
    address: "123456"
}])

function search() {
//将分页重置到第一页
    pageInfo.value.pageNum = 1
    // 调用分页接口
    getPageList()
}

/**
 * 重置搜索框
 */
function resetSearch() {
    searchFormComponents.value.resetFields()
    getPageList()
}

/**
 * 新增
 */
function add() {
    //清空表单中的数据
    formData.value = {}

    //将弹窗打开
    dialogOpen.value = true
}

/**
 * 编辑
 */
function edit(row) {
    //生成一个新的对象
    formData.value = Object.assign({}, row)
    dialogOpen.value = true
}

//选中的数据
const selectionRows = ref([])

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
    selectionRows.value = rows
}

function batchDelete(rows) {
    if (!rows) {
        rows = selectionRows.value
    }
    //取出每个对象中的id的值
    let ids = rows.map(item => item.id)
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
            request.delete("/petType/delBatch", {data: ids}).then((res => {
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
function deleteOne(row) {
    batchDelete([row])
}



const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()

/**
 * 关闭弹窗
 */
function closeDialog() {
    dialogOpen.value = false
}

/**
 * 提交数据
 */
function submit() {
    formRef.value.validate((valid) => {
        //校验
        if (!valid) {
            ElMessage({
                message: "验证失败，请检查表单",
                type: "warning"
            })
            return
        }
        //新增
        if (!formData.value.id) {
            //新增使用post
            request.post("/petType/add", formData.value).then(res => {
                if (!res) {
                    return
                }
                //关闭弹窗
                dialogOpen.value = false
                ElMessage({
                    message: "操作成功",
                    type: "success"
                })
                //重新获取列表
                getPageList()
            })
        } else {
            //修改使用put
            request.put("/petType/update", formData.value).then(res => {
                if (!res) {
                    return
                }
                //关闭弹窗
                dialogOpen.value = false
                ElMessage({
                    message: "操作成功",
                    type: "success"
                })
                //重新获取列表
                getPageList()
            })
        }

    })
}

</script>
```

### 22.宠物管理-后台宠物信息模块接口开发

##### sql-数据表

```
CREATE TABLE `pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `main_img` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面图',
  `pet_type_id` int(11) NOT NULL COMMENT '类型',
  `basic_info` text COLLATE utf8mb4_unicode_ci COMMENT '基本信息',
  `user_id` int(11) NOT NULL COMMENT '所属用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='宠物信息';
```

##### entity-实体类

```
package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 宠物信息
 */
@Data
public class Pet  {
   /**
    * id
    */
   private Integer id;
   /**
   * 名称
   */
   private String name;
   /**
   * 封面图
   */
   private String mainImg;
   /**
   * 类型
   */
   private Integer petTypeId;
   /**
   * 类型名称
   */
   private String petTypeName;
   /**
   * 基本信息
   */
   private String basicInfo;
   /**
   * 所属用户
   */
   private Integer userId;
   /**
   * 所属用户名称
   */
   private String username;
   /**
   * 创建时间
   */
   private LocalDateTime createTime;


   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getMainImg() {
      return mainImg;
   }

   public void setMainImg(String mainImg) {
      this.mainImg = mainImg;
   }

   public Integer getPetTypeId() {
      return petTypeId;
   }

   public void setPetTypeId(Integer petTypeId) {
      this.petTypeId = petTypeId;
   }

   public String getPetTypeName() {
      return petTypeName;
   }

   public void setPetTypeName(String petTypeName) {
      this.petTypeName = petTypeName;
   }

   public String getBasicInfo() {
      return basicInfo;
   }

   public void setBasicInfo(String basicInfo) {
      this.basicInfo = basicInfo;
   }

   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
```

##### mapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.PetMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.Pet">
        SELECT pet.*,pet_type.name AS petTypeName,user.username AS username
        FROM pet
        LEFT JOIN pet_type ON pet.pet_type_id=pet_type.id
        LEFT JOIN user ON pet.user_id=user.id
        <include refid="queryConditions"/>
        ORDER BY pet.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(pet.id) FROM pet
        LEFT JOIN pet_type ON pet.pet_type_id=pet_type.id
        LEFT JOIN user ON pet.user_id=user.id
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
            <if test="query.name != null and query.name.trim() != ''">
                AND pet.name LIKE CONCAT('%', #{query.name}, '%')
            </if>
            <if test="query.userId != null">
                AND pet.user_id = #{query.userId}
            </if>
            <if test="query.petTypeId != null">
                AND pet.pet_type_id = #{query.petTypeId}
            </if>
            <if test="query.username != null and query.username.trim() != ''">
                AND user.username LIKE CONCAT('%', #{query.username}, '%')
            </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.Pet" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO pet
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="name != null">name,</if>
            <if test="mainImg != null">main_img,</if>
            <if test="petTypeId != null">pet_type_id,</if>
            <if test="basicInfo != null">basic_info,</if>
            <if test="userId != null">user_id,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="name != null">#{name},</if>
            <if test="mainImg != null">#{mainImg},</if>
            <if test="petTypeId != null">#{petTypeId},</if>
            <if test="basicInfo != null">#{basicInfo},</if>
            <if test="userId != null">#{userId},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.Pet">
        UPDATE pet
        <set>
            <if test="name != null">
             name = #{name},
            </if>
            <if test="mainImg != null">
             main_img = #{mainImg},
            </if>
            <if test="petTypeId != null">
             pet_type_id = #{petTypeId},
            </if>
            <if test="basicInfo != null">
             basic_info = #{basicInfo},
            </if>
            <if test="userId != null">
             user_id = #{userId},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM pet
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
package com.project.platform.mapper;

import com.project.platform.entity.Pet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetMapper {
    List<Pet> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet WHERE id = #{id}")
    Pet selectById(Integer id);

    @Select("SELECT * FROM pet")
    List<Pet> list();

    int insert(Pet entity);

    int updateById(Pet entity);

    boolean removeByIds(List<Integer> ids);
}
```

##### service-接口（业务逻辑层）

```
package com.project.platform.service;

import com.project.platform.entity.Pet;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物信息
 */
public interface PetService {

    PageVO<Pet> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    Pet selectById(Integer id);

    List<Pet> list();

    void insert(Pet entity);

    void updateById(Pet entity);

    void removeByIds(List<Integer> id);
}
```

##### service.impl-实现类

```
package com.project.platform.service.impl;

import com.project.platform.entity.Pet;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetMapper;
import com.project.platform.service.PetService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物信息
 */
@Service
public class PetServiceImpl implements PetService {
    @Resource
    private PetMapper petMapper;

    @Override
    public PageVO<Pet> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Pet> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<Pet> list = petMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petMapper.queryCount(query));
        return page;
    }

    @Override
    public Pet selectById(Integer id) {
        Pet pet = petMapper.selectById(id);
        return pet;
    }

    @Override
    public List<Pet> list() {
        return petMapper.list();
    }

    @Override
    public void insert(Pet entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        petMapper.insert(entity);
    }

    @Override
    public void updateById(Pet entity) {
        check(entity);
        petMapper.updateById(entity);
    }

    private void check(Pet entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petMapper.removeByIds(ids);
    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.Pet;
import com.project.platform.service.PetService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物信息
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Resource
    private PetService petService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<Pet>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<Pet> page = petService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<Pet> selectById(@PathVariable("id") Integer id) {
        Pet entity = petService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<Pet>> list() {
        return ResponseVO.ok(petService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody Pet entity) {
        petService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody Pet entity) {
        petService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petService.removeByIds(ids);
        return ResponseVO.ok();
    }
}
```

##### router/index.js-路由文件

```
{
    path: 'pet',
    name: 'admin-pet',
    component: () =>
        import ('../views/admin/PetManage.vue')
},
```

##### layout/AdminLayout.vue-后台布局文件

```
  <el-menu-item index="/admin/pet">
    <el-icon>
      <Tickets/>
    </el-icon>
    <span>宠物信息</span>
  </el-menu-item>
```

##### PetManage.vue-后台管理页面-路由文件



```
<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="名称" prop="name">
            <el-input v-model="searchForm.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="类型" prop="petTypeId">
            <el-select v-model="searchForm.petTypeId" placeholder="请选择" clearable filterable style="width: 150px">
              <el-option :label="item.name"  :value="item.id" :key="item.id" v-for="item in petTypeList"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" @click="add" :icon="Plus">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">批量删除</el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  @selection-change="selectionChange"
                  border>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="mainImg" label="封面图">
            <template #default="scope" >
               <el-image v-if="scope.row.mainImg" style="width: 100px; height: 100px" :src="scope.row.mainImg" :preview-src-list="[scope.row.mainImg]" :preview-teleported="true" ></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="petTypeName" label="类型名称"></el-table-column>
          <el-table-column prop="username" label="所属用户名称"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <el-button :icon="Edit" @click="edit(scope.$index, scope.row)">编辑</el-button>
              <el-button :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
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
            width="800"
    >
      <el-form ref="formRef" :model="formData" label-width="100px">

          <el-form-item label="名称" prop="name"  :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.name"></el-input>
          </el-form-item>
          <el-form-item label="封面图" prop="mainImg"  :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <MyUpLoad type="imageCard" :limit="1" :files="formData.mainImg" @setFiles="formData.mainImg =$event" v-if="dialogOpen"></MyUpLoad>
          </el-form-item>
          <el-form-item label="类型" prop="petTypeId"  :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-select v-model="formData.petTypeId" placeholder="请选择" filterable>
              <el-option :label="item.name"  :value="item.id" :key="item.id" v-for="item in petTypeList"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="基本信息" prop="basicInfo"  >
            <MyEditor :content="formData.basicInfo" @content-change="formData.basicInfo =$event" v-if="dialogOpen"></MyEditor>
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
import MyUpLoad from "@/components/MyUpload.vue";

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
  name: undefined,
  petTypeId: undefined,
  username: undefined,

});

const  petTypeList=ref([])

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
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
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
    if (!valid){
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
const selectionRows = ref([]);

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
  selectionRows.value = rows
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
  if (!rows) {
    rows = selectionRows.value;
  }
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
</script>

<style scoped>

</style>
```

### 23.前台我的宠物模块开发

##### router/index.js-路由文件

```
{
    path: "myPet",
    name: "front-myPet",
    component: () =>
        import ('../views/front/MyPet.vue')
},
```

##### layout/FrontLayout.vue-前台布局文件

```
<el-menu-item index="/myPet">我的宠物</el-menu-item>
```

##### MyPet.vue-前台页面

```
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
```

### 24.后台宠物管理-宠物日记模块开发

##### sql-数据表

```
CREATE TABLE `pet_diary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `user_id` int(11) NOT NULL COMMENT '用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='宠物日记';
```

##### entity-实体类

```
package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 宠物日记
 */
@Data
public class PetDiary  {
   /**
    * id
    */
   private Integer id;
   /**
   * 标题
   */
   private String title;
   /**
   * 内容
   */
   private String content;
   /**
   * 用户
   */
   private Integer userId;
   /**
   * 用户名称
   */
   private String username;


   /**
    * 用户头像
    */
   private String userAvatarUrl;

   /**
   * 创建时间
   */
   private LocalDateTime createTime;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getUserAvatarUrl() {
      return userAvatarUrl;
   }

   public void setUserAvatarUrl(String userAvatarUrl) {
      this.userAvatarUrl = userAvatarUrl;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
```

##### mapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.PetDiaryMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.PetDiary">
        SELECT pet_diary.*
        ,user.username AS username
        ,user.avatar_url AS userAvatarUrl
        FROM pet_diary
        LEFT JOIN user ON pet_diary.user_id=user.id
        <include refid="queryConditions"/>
        ORDER BY pet_diary.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(pet_diary.id) FROM pet_diary
        LEFT JOIN user ON pet_diary.user_id=user.id
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
            <if test="query.title != null and query.title.trim() != ''">
                AND pet_diary.title LIKE CONCAT('%', #{query.title}, '%')
            </if>
            <if test="query.userId != null">
                AND pet_diary.user_id = #{query.userId}
            </if>
            <if test="query.username != null and query.username.trim() != ''">
                AND user.username LIKE CONCAT('%', #{query.username}, '%')
            </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.PetDiary" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO pet_diary
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="title != null">title,</if>
            <if test="content != null">content,</if>
            <if test="userId != null">user_id,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="title != null">#{title},</if>
            <if test="content != null">#{content},</if>
            <if test="userId != null">#{userId},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.PetDiary">
        UPDATE pet_diary
        <set>
            <if test="title != null">
             title = #{title},
            </if>
            <if test="content != null">
             content = #{content},
            </if>
            <if test="userId != null">
             user_id = #{userId},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM pet_diary
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
package com.project.platform.mapper;

import com.project.platform.entity.PetDiary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetDiaryMapper {
    List<PetDiary> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_diary WHERE id = #{id}")
    PetDiary selectById(Integer id);

    @Select("SELECT * FROM pet_diary")
    List<PetDiary> list();

    int insert(PetDiary entity);

    int updateById(PetDiary entity);

    boolean removeByIds(List<Integer> ids);

}
```

##### service-接口（业务逻辑层）

```
package com.project.platform.service;

import com.project.platform.entity.PetDiary;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物日记
 */
public interface PetDiaryService {

    PageVO<PetDiary> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetDiary selectById(Integer id);

    List<PetDiary> list();

    void insert(PetDiary entity);

    void updateById(PetDiary entity);

    void removeByIds(List<Integer> id);
}
```

##### service.impl-实现类

```
package com.project.platform.service.impl;

import com.project.platform.entity.PetDiary;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetDiaryMapper;
import com.project.platform.service.PetDiaryService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物日记
 */
@Service
public class PetDiaryServiceImpl implements PetDiaryService {
    @Resource
    private PetDiaryMapper petDiaryMapper;

    @Override
    public PageVO<PetDiary> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetDiary> page = new PageVO();
        if (query.containsKey("onlyYou") && Boolean.valueOf(query.get("onlyYou").toString())) {
            Integer userId = -1;
            if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
                //普通用户看自己才有效果
                userId = CurrentUserThreadLocal.getCurrentUser().getId();
            }
            query.put("userId", userId);
        }
        List<PetDiary> list = petDiaryMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petDiaryMapper.queryCount(query));
        return page;
    }

    @Override
    public PetDiary selectById(Integer id) {
        PetDiary petDiary = petDiaryMapper.selectById(id);
        return petDiary;
    }

    @Override
    public List<PetDiary> list() {
        return petDiaryMapper.list();
    }

    @Override
    public void insert(PetDiary entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        petDiaryMapper.insert(entity);
    }

    @Override
    public void updateById(PetDiary entity) {
        check(entity);
        petDiaryMapper.updateById(entity);
    }

    private void check(PetDiary entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petDiaryMapper.removeByIds(ids);
    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.PetDiary;
import com.project.platform.service.PetDiaryService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物日记
 */
@RestController
@RequestMapping("/petDiary")
public class PetDiaryController {
    @Resource
    private PetDiaryService petDiaryService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetDiary>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetDiary> page = petDiaryService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<PetDiary> selectById(@PathVariable("id") Integer id) {
        PetDiary entity = petDiaryService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetDiary>> list() {

        return ResponseVO.ok(petDiaryService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetDiary entity) {
        petDiaryService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetDiary entity) {
        petDiaryService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petDiaryService.removeByIds(ids);
        return ResponseVO.ok();
    }
}
```

##### router/index.js-路由文件

```
{
    path: 'petDiary',
    name: 'admin-petDiary',
    component: () =>
        import ('../views/admin/PetDiaryManage.vue')
}
,
```

##### layout/AdminLayout.vue-后台布局文件

```
<el-menu-item index="/admin/petDiary">
  <el-icon>
    <Edit/>
  </el-icon>
  <span>宠物日记</span>
</el-menu-item>
```

##### PetDiaryManage.vue-后台管理页面

```
<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="标题" prop="title">
            <el-input v-model="searchForm.title" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" @click="add" :icon="Plus">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">批量删除</el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  @selection-change="selectionChange"
                  border>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="username" label="用户名称"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <el-button :icon="Edit" @click="edit(scope.$index, scope.row)">编辑</el-button>
              <el-button :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
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
            width="800"
    >
      <el-form ref="formRef" :model="formData" label-width="100px">

          <el-form-item label="标题" prop="title"  :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.title"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content"  >
            <MyEditor :content="formData.content" @content-change="formData.content =$event" v-if="dialogOpen"></MyEditor>
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
  username: undefined,

});



getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petDiary/page", {
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
    if (!valid){
      ElMessage({
        message: "验证失败，请检查表单!",
        type: 'warning'
      });
      return
    }
    //新增
    if (!formData.value.id) {
      request.post("/petDiary/add", formData.value).then(res => {
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
      request.put("/petDiary/update", formData.value).then(res => {
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
const selectionRows = ref([]);

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
  selectionRows.value = rows
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
  if (!rows) {
    rows = selectionRows.value;
  }
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/petDiary/delBatch", {data: ids}).then(res => {
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
</script>

<style scoped>

</style>
```

### 25.前台宠物日记模块开发

##### router/index.js-路由文件

```
{
    path: "petDiary",
    name: "front-petDiary",
    component: () =>
        import ('../views/front/PetDiary.vue')
},
```

##### layout/FrontLayout.vue-前台布局文件

```
<el-menu-item index="/petDiary">宠物日记</el-menu-item>
```

##### PetDiary.vue-前台文件

```
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
          <el-button link type="plan" @click="router.push('/petDiaryDetails/'+item.id)">查看详情</el-button>
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
  request.get("/petDiary/page", {
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
      request.post("/petDiary/add", formData.value).then(res => {
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
      request.put("/petDiary/update", formData.value).then(res => {
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
    request.delete("/petDiary/delBatch", {data: ids}).then(res => {
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
```

### 26.宠物订单管理-喂养订单模块+前后端交互

##### sql-数据表

```
CREATE TABLE `pet_feed` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pet_id` int(11) NOT NULL COMMENT '宠物',
  `pet_type_id` int(11) NOT NULL COMMENT '宠物类型',
  `user_id` int(11) NOT NULL COMMENT '用户',
  `pet_store_manager_id` int(11) NOT NULL COMMENT '店长',
  `reserved_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '预约时间',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='宠物喂养';
```

entity-实体类

```
package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 宠物喂养
 */
@Data
public class PetFeed  {
   /**
    * id
    */
   private Integer id;
   /**
   * 宠物
   */
   private Integer petId;
   /**
   * 宠物名称
   */
   private String petName;
   /**
   * 宠物类型
   */
   private Integer petTypeId;
   /**
   * 宠物类型名称
   */
   private String petTypeName;
   /**
   * 用户
   */
   private Integer userId;
   /**
   * 用户名称
   */
   private String username;
   /**
   * 店长
   */
   private Integer petStoreManagerId;
   /**
   * 店铺名称
   */
   private String storeName;
   /**
   * 预约时间
   */
   private LocalDateTime reservedTime;
   /**
   * 备注
   */
   private String remark;
   /**
   * 状态
   */
   private String status;
   /**
   * 创建时间
   */
   private LocalDateTime createTime;


   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }
   public Integer getPetId() {
      return petId;
   }

   public void setPetId(Integer petId) {
      this.petId = petId;
   }

   public String getPetName() {
      return petName;
   }

   public void setPetName(String petName) {
      this.petName = petName;
   }

   public Integer getPetTypeId() {
      return petTypeId;
   }

   public void setPetTypeId(Integer petTypeId) {
      this.petTypeId = petTypeId;
   }

   public String getPetTypeName() {
      return petTypeName;
   }

   public void setPetTypeName(String petTypeName) {
      this.petTypeName = petTypeName;
   }

   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Integer getPetStoreManagerId() {
      return petStoreManagerId;
   }

   public void setPetStoreManagerId(Integer petStoreManagerId) {
      this.petStoreManagerId = petStoreManagerId;
   }

   public String getStoreName() {
      return storeName;
   }

   public void setStoreName(String storeName) {
      this.storeName = storeName;
   }

   public LocalDateTime getReservedTime() {
      return reservedTime;
   }

   public void setReservedTime(LocalDateTime reservedTime) {
      this.reservedTime = reservedTime;
   }

   public String getRemark() {
      return remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
```

##### mapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.PetFeedMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.PetFeed">
        SELECT pet_feed.*,pet_type.name AS petTypeName,pet_store_manager.store_name AS storeName,user.username AS username,pet.name AS petName
        FROM pet_feed
        LEFT JOIN pet_type ON pet_feed.pet_type_id=pet_type.id
        LEFT JOIN pet_store_manager ON pet_feed.pet_store_manager_id=pet_store_manager.id
        LEFT JOIN user ON pet_feed.user_id=user.id
        LEFT JOIN pet ON pet_feed.pet_id=pet.id
        <include refid="queryConditions"/>
        ORDER BY pet_feed.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(pet_feed.id) FROM pet_feed
        LEFT JOIN pet_type ON pet_feed.pet_type_id=pet_type.id
        LEFT JOIN pet_store_manager ON pet_feed.pet_store_manager_id=pet_store_manager.id
        LEFT JOIN user ON pet_feed.user_id=user.id
        LEFT JOIN pet ON pet_feed.pet_id=pet.id
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
            <if test="query.userId != null">
                AND pet_feed.user_id = #{query.userId}
            </if>
            <if test="query.petStoreManagerId != null">
                AND pet_feed.pet_store_manager_id = #{query.petStoreManagerId}
            </if>
            <if test="query.petName != null and query.petName.trim() != ''">
                AND pet.name LIKE CONCAT('%', #{query.petName}, '%')
            </if>
            <if test="query.username != null and query.username.trim() != ''">
                AND user.username LIKE CONCAT('%', #{query.username}, '%')
            </if>
            <if test="query.storeName != null and query.storeName.trim() != ''">
                AND pet_store_manager.store_name LIKE CONCAT('%', #{query.storeName}, '%')
            </if>
            <if test="query.remark != null and query.remark.trim() != ''">
                AND pet_feed.remark LIKE CONCAT('%', #{query.remark}, '%')
            </if>
            <if test="query.status != null and query.status.trim() != ''">
                AND pet_feed.status = #{query.status}
            </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.PetFeed" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO pet_feed
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="petId != null">pet_id,</if>
            <if test="petTypeId != null">pet_type_id,</if>
            <if test="userId != null">user_id,</if>
            <if test="petStoreManagerId != null">pet_store_manager_id,</if>
            <if test="reservedTime != null">reserved_time,</if>
            <if test="remark != null">remark,</if>
            <if test="status != null">status,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="petId != null">#{petId},</if>
            <if test="petTypeId != null">#{petTypeId},</if>
            <if test="userId != null">#{userId},</if>
            <if test="petStoreManagerId != null">#{petStoreManagerId},</if>
            <if test="reservedTime != null">#{reservedTime},</if>
            <if test="remark != null">#{remark},</if>
            <if test="status != null">#{status},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.PetFeed">
        UPDATE pet_feed
        <set>
            <if test="petId != null">
             pet_id = #{petId},
            </if>
            <if test="petTypeId != null">
             pet_type_id = #{petTypeId},
            </if>
            <if test="userId != null">
             user_id = #{userId},
            </if>
            <if test="petStoreManagerId != null">
             pet_store_manager_id = #{petStoreManagerId},
            </if>
            <if test="reservedTime != null">
             reserved_time = #{reservedTime},
            </if>
            <if test="remark != null">
             remark = #{remark},
            </if>
            <if test="status != null">
             status = #{status},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM pet_feed
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
package com.project.platform.mapper;

import com.project.platform.entity.PetFeed;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetFeedMapper {
    List<PetFeed> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_feed WHERE id = #{id}")
    PetFeed selectById(Integer id);

    @Select("SELECT * FROM pet_feed")
    List<PetFeed> list();

    int insert(PetFeed entity);

    int updateById(PetFeed entity);

    boolean removeByIds(List<Integer> ids);

}
```

##### service-接口（业务逻辑层）

```
package com.project.platform.service;

import com.project.platform.entity.PetFeed;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物喂养
 */
public interface PetFeedService {

    PageVO<PetFeed> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetFeed selectById(Integer id);

    List<PetFeed> list();

    void insert(PetFeed entity);

    void updateById(PetFeed entity);

    void removeByIds(List<Integer> id);

    void inService(Integer id);

    void finish(Integer id);
}
```

##### service.impl-实现类

```
package com.project.platform.service.impl;

import com.project.platform.entity.PetFeed;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetFeedMapper;
import com.project.platform.service.PetFeedService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物喂养
 */
@Service
public class PetFeedServiceImpl implements PetFeedService {
    @Resource
    private PetFeedMapper petFeedMapper;

    @Override
    public PageVO<PetFeed> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetFeed> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        } else if (CurrentUserThreadLocal.getCurrentUser().getType().equals("PET_STORE_MANAGER")) {
            query.put("petStoreManagerId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<PetFeed> list = petFeedMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petFeedMapper.queryCount(query));
        return page;
    }

    @Override
    public PetFeed selectById(Integer id) {
        PetFeed petFeed = petFeedMapper.selectById(id);
        return petFeed;
    }

    @Override
    public List<PetFeed> list() {
        return petFeedMapper.list();
    }

    @Override
    public void insert(PetFeed entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setStatus("已下单");
        check(entity);
        petFeedMapper.insert(entity);
    }

    @Override
    public void updateById(PetFeed entity) {
        check(entity);
        petFeedMapper.updateById(entity);
    }

    private void check(PetFeed entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petFeedMapper.removeByIds(ids);
    }


    @Override
    public void inService(Integer id) {
        PetFeed entity = petFeedMapper.selectById(id);
        entity.setStatus("服务中");
        petFeedMapper.updateById(entity);
    }

    @Override
    public void finish(Integer id) {
        PetFeed entity = petFeedMapper.selectById(id);
        entity.setStatus("已完成");
        petFeedMapper.updateById(entity);
    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.PetFeed;
import com.project.platform.service.PetFeedService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物喂养
 */
@RestController
@RequestMapping("/petFeed")
public class PetFeedController {
    @Resource
    private PetFeedService petFeedService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetFeed>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetFeed> page = petFeedService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public  ResponseVO<PetFeed> selectById(@PathVariable("id") Integer id) {
        PetFeed entity = petFeedService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetFeed>> list() {

return ResponseVO.ok(petFeedService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetFeed entity) {
        petFeedService.insert(entity);
return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetFeed entity) {
        petFeedService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petFeedService.removeByIds(ids);
        return ResponseVO.ok();
    }

    @PostMapping("inService/{id}")
    public ResponseVO inService(@PathVariable("id") Integer id) {
        petFeedService.inService(id);
        return ResponseVO.ok();
    }

    @PostMapping("finish/{id}")
    public ResponseVO finish(@PathVariable("id") Integer id) {
        petFeedService.finish(id);
        return ResponseVO.ok();
    }
}
```

##### router/index.js-路由文件

```
{
    path: 'petFeed',
    name: 'admin-petFeed',
    component: () =>
        import ('../views/admin/PetFeedManage.vue')
},
```

##### layout/AdminLayout.vue-后台布局文件

```
<el-sub-menu index="2">
  <template #title>
    <el-icon>
      <TakeawayBox/>
    </el-icon>
    <span>宠物订单</span>
  </template>
  <el-menu-item index="/admin/petFeed">
    <el-icon>
      <User/>
    </el-icon>
    <span>喂养订单</span>
  </el-menu-item>
</el-sub-menu>
```

##### PetFeedManage.vue-后台管理页面

```
<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="宠物名称" prop="petName">
            <el-input v-model="searchForm.petName" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="店铺名称" prop="storeName">
            <el-input v-model="searchForm.storeName" clearable></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="searchForm.remark" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable filterable style="width: 150px">
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" @click="add" :icon="Plus">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">
            批量删除
          </el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  @selection-change="selectionChange"
                  border>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="petName" label="宠物名称" width="100"></el-table-column>
          <el-table-column prop="petTypeName" label="宠物类型名称" width="120"></el-table-column>
          <el-table-column prop="username" label="用户名称"></el-table-column>
          <el-table-column prop="storeName" label="店铺名称"></el-table-column>
          <el-table-column prop="reservedTime" label="预约时间"></el-table-column>
          <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='已下单'" type="success">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='服务中'" type="warning">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='已完成'" type="info">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="高级操作" width="120">
            <template #default="scope">
              <el-button type="primary" @click="inService(scope.row)" v-if="scope.row.status==='已下单'">进行服务
              </el-button>
              <el-button type="success" @click="finish(scope.row)" v-if="scope.row.status==='服务中'">完成服务
              </el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
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

          <el-form-item label="宠物" prop="petId"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.petId"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" :rows="5" v-model="formData.remark"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-select v-model="formData.status" placeholder="请选择" filterable>
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
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
  petName: undefined,
  username: undefined,
  storeName: undefined,
  remark: undefined,
  status: undefined,

});

const statusList = ref(['已下单', '服务中', '已完成'])


getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petFeed/page", {
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
      request.post("/petFeed/add", formData.value).then(res => {
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
      request.put("/petFeed/update", formData.value).then(res => {
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

const selectionRows = ref([]);

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
  selectionRows.value = rows
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
  if (!rows) {
    rows = selectionRows.value;
  }
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/petFeed/delBatch", {data: ids}).then(res => {
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

function inService(row) {
  request.post("/petFeed/inService/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList()
  })
}

function finish(row) {
  request.post("/petFeed/finish/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList()
  })
}
</script>

<style scoped>

</style>
```

### 27.宠物订单管理-寄养订单模块+前后端交互

##### sql-数据表

```
CREATE TABLE `pet_foster_care` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pet_id` int NOT NULL COMMENT '宠物',
  `pet_type_id` int NOT NULL COMMENT '宠物类型',
  `user_id` int NOT NULL COMMENT '用户',
  `pet_store_manager_id` int NOT NULL COMMENT '店长',
  `reserved_start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '预约开始时间',
  `reserved_end_time` timestamp NULL DEFAULT NULL COMMENT '预约结束时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='宠物寄养';
```

##### entity-实体类

```
package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 宠物寄养
 */
@Data
public class PetFosterCare  {
   /**
    * id
    */
   private Integer id;
   /**
   * 宠物
   */
   private Integer petId;
   /**
   * 宠物名称
   */
   private String petName;
   /**
   * 宠物类型
   */
   private Integer petTypeId;
   /**
   * 宠物类型名称
   */
   private String petTypeName;
   /**
   * 用户
   */
   private Integer userId;
   /**
   * 用户名称
   */
   private String username;
   /**
   * 店长
   */
   private Integer petStoreManagerId;
   /**
   * 店铺名称
   */
   private String storeName;
   /**
   * 预约开始时间
   */
   private LocalDateTime reservedStartTime;
   /**
   * 预约结束时间
   */
   private LocalDateTime reservedEndTime;
   /**
   * 备注
   */
   private String remark;
   /**
   * 状态
   */
   private String status;
   /**
   * 创建时间
   */
   private LocalDateTime createTime;


   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }
   public Integer getPetId() {
      return petId;
   }

   public void setPetId(Integer petId) {
      this.petId = petId;
   }

   public String getPetName() {
      return petName;
   }

   public void setPetName(String petName) {
      this.petName = petName;
   }

   public Integer getPetTypeId() {
      return petTypeId;
   }

   public void setPetTypeId(Integer petTypeId) {
      this.petTypeId = petTypeId;
   }

   public String getPetTypeName() {
      return petTypeName;
   }

   public void setPetTypeName(String petTypeName) {
      this.petTypeName = petTypeName;
   }

   public Integer getUserId() {
      return userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Integer getPetStoreManagerId() {
      return petStoreManagerId;
   }

   public void setPetStoreManagerId(Integer petStoreManagerId) {
      this.petStoreManagerId = petStoreManagerId;
   }

   public String getStoreName() {
      return storeName;
   }

   public void setStoreName(String storeName) {
      this.storeName = storeName;
   }

   public LocalDateTime getReservedStartTime() {
      return reservedStartTime;
   }

   public void setReservedStartTime(LocalDateTime reservedStartTime) {
      this.reservedStartTime = reservedStartTime;
   }

   public LocalDateTime getReservedEndTime() {
      return reservedEndTime;
   }

   public void setReservedEndTime(LocalDateTime reservedEndTime) {
      this.reservedEndTime = reservedEndTime;
   }

   public String getRemark() {
      return remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
```

##### mapper.xml- sql 映射文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.platform.mapper.PetFosterCareMapper">
    <!-- 分页查询 -->
    <select id="queryPage"  resultType="com.project.platform.entity.PetFosterCare">
        SELECT pet_foster_care.*,pet_type.name AS petTypeName,pet_store_manager.store_name AS storeName,user.username AS username,pet.name AS petName
        FROM pet_foster_care
        LEFT JOIN pet_type ON pet_foster_care.pet_type_id=pet_type.id
        LEFT JOIN pet_store_manager ON pet_foster_care.pet_store_manager_id=pet_store_manager.id
        LEFT JOIN user ON pet_foster_care.user_id=user.id
        LEFT JOIN pet ON pet_foster_care.pet_id=pet.id
        <include refid="queryConditions"/>
        ORDER BY pet_foster_care.id DESC
        LIMIT #{offset}, #{pageSize}
    </select>
    <!-- 查询总数 -->
    <select id="queryCount"  resultType="int">
        SELECT count(pet_foster_care.id) FROM pet_foster_care
        LEFT JOIN pet_type ON pet_foster_care.pet_type_id=pet_type.id
        LEFT JOIN pet_store_manager ON pet_foster_care.pet_store_manager_id=pet_store_manager.id
        LEFT JOIN user ON pet_foster_care.user_id=user.id
        LEFT JOIN pet ON pet_foster_care.pet_id=pet.id
        <include refid="queryConditions"/>
    </select>

    <!-- 查询条件 -->
    <sql id="queryConditions">
        <where>
            <if test="query.userId != null">
                AND pet_foster_care.user_id = #{query.userId}
            </if>
            <if test="query.petStoreManagerId != null">
                AND pet_foster_care.pet_store_manager_id = #{query.petStoreManagerId}
            </if>
            <if test="query.petName != null and query.petName.trim() != ''">
                AND pet.name LIKE CONCAT('%', #{query.petName}, '%')
            </if>
            <if test="query.username != null and query.username.trim() != ''">
                AND user.username LIKE CONCAT('%', #{query.username}, '%')
            </if>
            <if test="query.storeName != null and query.storeName.trim() != ''">
                AND pet_store_manager.store_name LIKE CONCAT('%', #{query.storeName}, '%')
            </if>
            <if test="query.remark != null and query.remark.trim() != ''">
                AND pet_foster_care.remark LIKE CONCAT('%', #{query.remark}, '%')
            </if>
            <if test="query.status != null and query.status.trim() != ''">
                AND pet_foster_care.status = #{query.status}
            </if>
        </where>
    </sql>

    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.PetFosterCare" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO pet_foster_care
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="petId != null">pet_id,</if>
            <if test="petTypeId != null">pet_type_id,</if>
            <if test="userId != null">user_id,</if>
            <if test="petStoreManagerId != null">pet_store_manager_id,</if>
            <if test="reservedStartTime != null">reserved_start_time,</if>
            <if test="reservedEndTime != null">reserved_end_time,</if>
            <if test="remark != null">remark,</if>
            <if test="status != null">status,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="petId != null">#{petId},</if>
            <if test="petTypeId != null">#{petTypeId},</if>
            <if test="userId != null">#{userId},</if>
            <if test="petStoreManagerId != null">#{petStoreManagerId},</if>
            <if test="reservedStartTime != null">#{reservedStartTime},</if>
            <if test="reservedEndTime != null">#{reservedEndTime},</if>
            <if test="remark != null">#{remark},</if>
            <if test="status != null">#{status},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.PetFosterCare">
        UPDATE pet_foster_care
        <set>
            <if test="petId != null">
             pet_id = #{petId},
            </if>
            <if test="petTypeId != null">
             pet_type_id = #{petTypeId},
            </if>
            <if test="userId != null">
             user_id = #{userId},
            </if>
            <if test="petStoreManagerId != null">
             pet_store_manager_id = #{petStoreManagerId},
            </if>
            <if test="reservedStartTime != null">
             reserved_start_time = #{reservedStartTime},
            </if>
            <if test="reservedEndTime != null">
             reserved_end_time = #{reservedEndTime},
            </if>
            <if test="remark != null">
             remark = #{remark},
            </if>
            <if test="status != null">
             status = #{status},
            </if>
        </set>
        WHERE id = #{id}
    </update>


    <!-- 根据ID列表删除 -->
    <delete id="removeByIds">
        DELETE FROM pet_foster_care
        WHERE id IN
        <foreach item="id" collection="list" open="(" separator="," close=")">
            #{id}
        </foreach>
    </delete>    
</mapper>
```

##### mapper-数据访问层

```
package com.project.platform.mapper;

import com.project.platform.entity.PetFosterCare;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetFosterCareMapper {
    List<PetFosterCare> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_foster_care WHERE id = #{id}")
    PetFosterCare selectById(Integer id);

    @Select("SELECT * FROM pet_foster_care")
    List<PetFosterCare> list();

    int insert(PetFosterCare entity);

    int updateById(PetFosterCare entity);

    boolean removeByIds(List<Integer> ids);

}
```

##### service-接口（业务逻辑层）

```
package com.project.platform.service;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物寄养
 */
public interface PetFosterCareService {

    PageVO<PetFosterCare> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetFosterCare selectById(Integer id);

    List<PetFosterCare> list();

    void insert(PetFosterCare entity);

    void updateById(PetFosterCare entity);

    void removeByIds(List<Integer> id);

    void inService(Integer id);

    void finish(Integer id);
}
```

##### service.impl-实现类

```
package com.project.platform.service.impl;

import com.project.platform.entity.PetFeed;
import com.project.platform.entity.PetFosterCare;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetFosterCareMapper;
import com.project.platform.service.PetFosterCareService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物寄养
 */
@Service
public class PetFosterCareServiceImpl implements PetFosterCareService {
    @Resource
    private PetFosterCareMapper petFosterCareMapper;

    @Override
    public PageVO<PetFosterCare> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetFosterCare> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        } else if (CurrentUserThreadLocal.getCurrentUser().getType().equals("PET_STORE_MANAGER")) {
            query.put("petStoreManagerId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<PetFosterCare> list = petFosterCareMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petFosterCareMapper.queryCount(query));
        return page;
    }

    @Override
    public PetFosterCare selectById(Integer id) {
        PetFosterCare petFosterCare = petFosterCareMapper.selectById(id);
        return petFosterCare;
    }

    @Override
    public List<PetFosterCare> list() {
        return petFosterCareMapper.list();
    }

    @Override
    public void insert(PetFosterCare entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setStatus("已下单");
        check(entity);
        petFosterCareMapper.insert(entity);
    }

    @Override
    public void updateById(PetFosterCare entity) {
        check(entity);
        petFosterCareMapper.updateById(entity);
    }

    private void check(PetFosterCare entity) {
        if (entity.getReservedStartTime().isAfter(entity.getReservedEndTime())) {
            throw new CustomException("开始时间不能大于结束时间");
        }

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petFosterCareMapper.removeByIds(ids);
    }

    @Override
    public void inService(Integer id) {
        PetFosterCare entity = petFosterCareMapper.selectById(id);
        entity.setStatus("服务中");
        petFosterCareMapper.updateById(entity);
    }

    @Override
    public void finish(Integer id) {
        PetFosterCare entity = petFosterCareMapper.selectById(id);
        entity.setStatus("已完成");
        petFosterCareMapper.updateById(entity);
    }
}
```

##### controller-控制器

```
package com.project.platform.controller;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.service.PetFosterCareService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物寄养
 */
@RestController
@RequestMapping("/petFosterCare")
public class PetFosterCareController {
    @Resource
    private PetFosterCareService petFosterCareService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetFosterCare>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetFosterCare> page = petFosterCareService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public  ResponseVO<PetFosterCare> selectById(@PathVariable("id") Integer id) {
        PetFosterCare entity = petFosterCareService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetFosterCare>> list() {
return ResponseVO.ok(petFosterCareService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetFosterCare entity) {
        petFosterCareService.insert(entity);
return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetFosterCare entity) {
        petFosterCareService.updateById(entity);
return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petFosterCareService.removeByIds(ids);
return ResponseVO.ok();
    }

    @PostMapping("inService/{id}")
    public ResponseVO inService(@PathVariable("id") Integer id) {
        petFosterCareService.inService(id);
        return ResponseVO.ok();

    }

    @PostMapping("finish/{id}")
    public ResponseVO finish(@PathVariable("id") Integer id) {
        petFosterCareService.finish(id);
        return ResponseVO.ok();
    }
}
```

##### router/index.js-路由文件

```
{
    path: 'petFosterCare',
    name: 'admin-petFosterCare',
    component: () =>
        import ('../views/admin/PetFosterCareManage.vue')
},
```

##### layout/AdminLayout.vue-后台布局文件

```
  <el-menu-item index="/admin/petFosterCare">
    <el-icon>
      <el-icon>
        <CoffeeCup/>
      </el-icon>
    </el-icon>
    <span>寄养订单</span>
  </el-menu-item>
```

##### PetFosterCareManage.vue-后台管理页面

```
<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="宠物名称" prop="petName">
            <el-input v-model="searchForm.petName" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="店铺名称" prop="storeName">
            <el-input v-model="searchForm.storeName" clearable></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="searchForm.remark" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable filterable style="width: 150px">
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" @click="add" :icon="Plus">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">
            批量删除
          </el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  @selection-change="selectionChange"
                  border>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="petName" label="宠物名称" width="100"></el-table-column>
          <el-table-column prop="petTypeName" label="宠物类型名称" width="120"></el-table-column>
          <el-table-column prop="username" label="用户名称"></el-table-column>
          <el-table-column prop="storeName" label="店铺名称"></el-table-column>
          <el-table-column prop="reservedStartTime" label="预约开始时间"></el-table-column>
          <el-table-column prop="reservedEndTime" label="预约结束时间"></el-table-column>
          <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='已下单'" type="success">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='服务中'" type="warning">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='已完成'" type="info">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="高级操作" width="120">
            <template #default="scope">
              <el-button type="primary" @click="inService(scope.row)" v-if="scope.row.status==='已下单'">进行服务
              </el-button>
              <el-button type="success" @click="finish(scope.row)" v-if="scope.row.status==='服务中'">完成服务
              </el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
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

          <el-form-item label="宠物" prop="petId"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-input v-model="formData.petId"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" :rows="5" v-model="formData.remark"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status"
                        :rules="[{required:true,message:'不能为空',trigger:[ 'blur','change']}]">
            <el-select v-model="formData.status" placeholder="请选择" filterable>
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
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
  petName: undefined,
  username: undefined,
  storeName: undefined,
  remark: undefined,
  status: undefined,

});

const statusList = ref(['已下单', '服务中', '已完成'])


getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petFosterCare/page", {
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
      request.post("/petFosterCare/add", formData.value).then(res => {
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
      request.put("/petFosterCare/update", formData.value).then(res => {
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

const selectionRows = ref([]);

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
  selectionRows.value = rows
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
  if (!rows) {
    rows = selectionRows.value;
  }
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/petFosterCare/delBatch", {data: ids}).then(res => {
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

function inService(row) {
  request.post("/petFosterCare/inService/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList()
  })
}

function finish(row) {
  request.post("/petFosterCare/finish/" + row.id).then(res => {
    if (!res) {
      return
    }
    ElMessage({
      message: "操作成功",
      type: 'success'
    });
    getPageList()
  })
}

</script>

<style scoped>

</style>
```

### 28.个人中心模块搭建

##### router/index.js-路由文件

```
{
    path: 'personalCenter',
    name: 'personalCenter',
    component: () => import('../views/front/PersonalCenter.vue')
},
```

##### layout/FrontLayout.vue-前台布局组件

```
<el-menu-item index="/personalCenter">个人中心</el-menu-item>
```

##### PersonalCenter.vue-前台页面

```
<template>
  <div style="width: 75%;margin: 0 auto" :key="activeName">
    <el-card>
      <el-tabs v-model="activeName" tab-position="left">

      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useRoute} from "vue-router";

const activeName = ref('petFosterCare')
const route = useRoute()
if (route.query.type) {
  activeName.value = route.query.type
}
</script>

<style>

</style>
```

### 29.个人中心-寄养和喂养订单管理模块+前后端交互

##### personalCenter/PetFeed.vue-临时寄养订单

```
<template>
  <div style="margin-left: 5px">
    <el-tabs v-model="searchForm.status" @tab-change="getPageList">
      <el-tab-pane label="全部" name=""></el-tab-pane>
      <el-tab-pane label="已下单" name="已下单"></el-tab-pane>
      <el-tab-pane label="服务中" name="服务中"></el-tab-pane>
      <el-tab-pane label="已完成" name="已完成"></el-tab-pane>
    </el-tabs>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  border>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="petName" label="宠物名称" width="100"></el-table-column>
          <el-table-column prop="petTypeName" label="宠物类型名称" width="120"></el-table-column>
          <el-table-column prop="storeName" label="店铺名称"></el-table-column>
          <el-table-column prop="reservedTime" label="预约时间"></el-table-column>
          <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='已下单'" type="success">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='服务中'" type="warning">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='已完成'" type="info">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="80">
            <template #default="scope">
              <el-button link :disabled="scope.row.status!=='已下单'" type="danger"
                         @click="deleteOne(scope.$index, scope.row)">删除
              </el-button>
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
  request.get("/petFeed/page", {
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
    request.delete("/petFeed/delBatch", {data: ids}).then(res => {
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

</style>
```

##### personalCenter/PetFosterCare.vue-上门喂养订单

```
<template>
  <div style="margin-left: 5px">
    <el-tabs v-model="searchForm.status" @tab-change="getPageList">
      <el-tab-pane label="全部" name=""></el-tab-pane>
      <el-tab-pane label="已下单" name="已下单"></el-tab-pane>
      <el-tab-pane label="服务中" name="服务中"></el-tab-pane>
      <el-tab-pane label="已完成" name="已完成"></el-tab-pane>
    </el-tabs>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  border>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="petName" label="宠物名称" width="100"></el-table-column>
          <el-table-column prop="petTypeName" label="宠物类型名称" width="120"></el-table-column>
          <el-table-column prop="storeName" label="店铺名称"></el-table-column>
          <el-table-column prop="reservedStartTime" label="预约开始时间"></el-table-column>
          <el-table-column prop="reservedEndTime" label="预约结束时间"></el-table-column>
          <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='已下单'" type="success">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='服务中'" type="warning">{{ scope.row.status }}</el-tag>
              <el-tag v-else-if="scope.row.status==='已完成'" type="info">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="80">
            <template #default="scope">
              <el-button link :disabled="scope.row.status!=='已下单'" type="danger"
                         @click="deleteOne(scope.$index, scope.row)">删除
              </el-button>
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
  request.get("/petFosterCare/page", {
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
    request.delete("/petFosterCare/delBatch", {data: ids}).then(res => {
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

</style>
```

##### PersonalCenter.vue-前台页面

```
<el-tab-pane label="临时寄养订单" name="petFosterCare">
  <PetFosterCare v-if="activeName==='petFosterCare'"></PetFosterCare>
</el-tab-pane>
<el-tab-pane label="上门喂养订单" name="petFeed">
  <PetFeed v-if="activeName==='petFeed'"></PetFeed>
</el-tab-pane>

import PetFosterCare from "@/views/front/personalCenter/PetFosterCare.vue";
import PetFeed from "@/views/front/personalCenter/PetFeed.vue";
```

### 30.充值模块+前后端交互

##### sql-数据表

```
  `balance` float DEFAULT NULL COMMENT '余额',
```

##### User-实体类

```
   /**
   * 余额
   */
   private Float balance;
```

##### router/index.js-路由文件

```
 {
    path: "balanceInfo",
    name: "front-balanceInfo",
    component: () =>
        import ('../views/BalanceInfo.vue')
}
```

##### layout/FrontLayout.vue-后台布局文件

```
<el-dropdown-item><span @click="balanceInfo">余额/充值</span></el-dropdown-item>

function balanceInfo() {
  router.push({path: "/balanceInfo"})
}
```

##### BalanceInfo.vue充值页面-后台管理页面

```
<template>
  <div style="width:600px;margin: 0 auto">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>余额/充值</span>
        </div>
      </template>
      <el-space direction="vertical" alignment="left">
        <el-statistic title="当前余额" :value="userInfo.balance"/>
        <el-button type="primary" @click="topUp">充值</el-button>
      </el-space>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import http from "@/utils/http.js";
import {ElMessage, ElMessageBox} from "element-plus";
const userInfo = ref({});

load();

function load() {
  http.get('/common/currentUser').then(res => {
    userInfo.value = res.data;
  })
}

function topUp() {
  ElMessageBox.prompt('请输入充值金额', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^[1-9]\d*$/, // 至少输入一个字符
    inputErrorMessage: '充值金额不能为空' // 当输入不满足正则表达式时显示的错误信息
  }).then(({value}) => {
    http.post('/user/topUp/' + value).then(res => {
      ElMessage({
        message: '充值成功',
        type: 'success'
      });
      load();
    });
  })
};
</script>
```

##### UserController.vue

```
@PostMapping("/topUp/{amount}")
public ResponseVO topUp(@PathVariable Float amount) {
    Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
    userService.topUp(userId, amount);
    return ResponseVO.ok();
}
```

##### UserService

```
    void topUp(Integer userId, Float amount);

    void consumption(Integer userId, Float amount);
```

##### UserServiceImpl

```
    /**
     * 充值
     *
     * @param userId
     * @param amount
     */

    public void topUp(Integer userId, Float amount) {
        User user = selectById(userId);
        user.setBalance(user.getBalance() + amount);
        userMapper.updateById(user);
    }

    /**
     * 消费
     *
     * @param userId
     * @param amount
     */
    public void consumption(Integer userId, Float amount) {
        User user = selectById(userId);
        user.setBalance(user.getBalance() - amount);
        if (user.getBalance() < 0) {
            throw new CustomException("余额不足");
        }
        userMapper.updateById(user);
    }
```

##### CurrentUserDTO-数据转换对象

```
/**
 * 余额
 */
private Float balance;
```

##### UserMapper.xml

```
    <!-- 插入 -->
    <insert id="insert" parameterType="com.project.platform.entity.User" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO user
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="username != null">username,</if>
            <if test="password != null">password,</if>
            <if test="nickname != null">nickname,</if>
            <if test="avatarUrl != null">avatar_url,</if>
            <if test="tel != null">tel,</if>
            <if test="email != null">email,</if>
            <if test="status != null">status,</if>
            <if test="balance != null">balance,</if>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="username != null">#{username},</if>
            <if test="password != null">#{password},</if>
            <if test="nickname != null">#{nickname},</if>
            <if test="avatarUrl != null">#{avatarUrl},</if>
            <if test="tel != null">#{tel},</if>
            <if test="email != null">#{email},</if>
            <if test="status != null">#{status},</if>
            <if test="balance != null">#{balance},</if>
        </trim>


    </insert>

    <!-- 更新 -->
    <update id="updateById" parameterType="com.project.platform.entity.User">
        UPDATE user
        <set>
            <if test="username != null">
             username = #{username},
            </if>
            <if test="password != null">
             password = #{password},
            </if>
            <if test="nickname != null">
             nickname = #{nickname},
            </if>
            <if test="avatarUrl != null">
             avatar_url = #{avatarUrl},
            </if>
            <if test="tel != null">
             tel = #{tel},
            </if>
            <if test="email != null">
             email = #{email},
            </if>
            <if test="status != null">
             status = #{status},
            </if>
            <if test="balance != null">
             balance = #{balance},
            </if>
        </set>
        WHERE id = #{id}
    </update>
```

### 31.

### 32.

### 33.

### 34.

### 35.

### 36.

### 37.
