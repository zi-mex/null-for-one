<template>
  <div style="width: 75%;margin: 0 auto">
    <el-card>
      <el-divider content-position="left">基本信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-image :src="info.mainImg" style="height: 250px; width: 100%"/>
        </el-col>
        <el-col :span="16">
          <el-space direction="vertical" alignment="left" style="width: 100%">
            <div style="font-size: 24px">
              <span>价格:</span>
              <span style="color: red">￥{{ info.price }} </span>
            </div>
            <div>
              <el-space size="large">
                <el-statistic title="库存" :value="info.stock"/>
                <el-statistic title="销量" :value="info.salesVolume"/>
              </el-space>
            </div>
            <div>
              <el-space direction="vertical" alignment="left">
                <el-space spacer="|">
                  <el-tag type="success">预计5小时内发货</el-tag>
                  <span>承诺24小时内发货，超时必赔</span>
                </el-space>
                <el-space spacer="|">
                <span>
                    <el-icon :size="18" color="green"><CircleCheck/></el-icon> 正品险</span>
                  <span>100%正品保障，中国人寿财险承保</span>
                </el-space>

                <el-space spacer="|">
                  <span>全场包邮</span>
                  <span>退货包运费</span>
                  <span>假一赔十</span>
                  <span>降价补差</span>
                </el-space>
              </el-space>
            </div>
            <div>
              <el-button type="danger" @click="buy">立即购买</el-button>
            </div>
          </el-space>

        </el-col>
      </el-row>

      <el-divider content-position="left">图文详情</el-divider>
      <div v-html="info.introduce"></div>

    </el-card>


    <el-dialog
        v-model="buyDialogOpen"
        v-if="buyDialogOpen"
        title="选择收货地址"
        width="500"
    >
      <el-form ref="buyFormRef" :model="buyFormData" label-width="100px">

        <el-form-item label="收货地址" prop="shippingAddressId"
                      :rules="[{required:true,message:'请输入选择地址',trigger:[ 'blur','change']}]">
          <el-select v-model="buyFormData.shippingAddressId" placeholder="请选择" filterable>
            <el-option :label="item.name+'_'+item.tel+'_'+item.address" :value="item.id" :key="item.id"
                       v-for="item in shippingAddressList"></el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="buySubmit" :icon="Check">提交</el-button>
          <el-button @click="buyDialogOpen=false" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>


  </div>
</template>
<script setup>
import request from "@/utils/http.js";
import {ref} from "vue";
import {useRoute} from "vue-router";
import {Check, Close} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const route = useRoute()
const id = ref(route.params.id)
const info = ref({});

getInfo()

function getInfo() {
  request.get("/product/selectById/" + id.value).then(res => {
    info.value = res.data;
  })
}


const shippingAddressList = ref([])
getShippingAddressList();

async function getShippingAddressList() {
  let data = {
    pageNum: 1,
    pageSize: 100
  }
  request.get("/shippingAddress/page", {
    params: data
  }).then(res => {
    shippingAddressList.value = res.data.list;
  })
}

const buyDialogOpen = ref(false)
const buyFormData = ref({})
const buyFormRef = ref()

function buy() {
  buyDialogOpen.value = true
  buyFormData.value = {}
}

function buySubmit() {
  buyFormRef.value.validate((valid) => {
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查表单!",
        type: 'warning'
      });
      return
    }
    let shippingAddress = shippingAddressList.value.find(item => item.id == buyFormData.value.shippingAddressId);
    buyFormData.value.nameOfConsignee = shippingAddress.name;
    buyFormData.value.telOfConsignee = shippingAddress.tel;
    buyFormData.value.addressOfConsignee = shippingAddress.address;
    buyFormData.value.productId = id.value;
    request.post("/productOrder/add", buyFormData.value).then(res => {
      if (!res) {
        return
      }
      buyDialogOpen.value = false
      ElMessage({
        message: "操作成功",
        type: 'success'
      });
      router.push("/personalCenter?type=productOrder")
    })
  })
}


</script>

<style scoped>

</style>
