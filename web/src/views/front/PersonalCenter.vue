<template>
  <div style="width: 75%;margin: 0 auto" :key="activeName">
    <el-card>
      <el-tabs v-model="activeName" tab-position="left">
        <el-tab-pane label="临时寄养订单" name="petFosterCare">
          <PetFosterCare v-if="activeName==='petFosterCare'"></PetFosterCare>
        </el-tab-pane>
        <el-tab-pane label="上门喂养订单" name="petFeed">
          <PetFeed v-if="activeName==='petFeed'"></PetFeed>
        </el-tab-pane>
        <el-tab-pane label="收货地址" name="shippingAddress">
          <ShippingAddress v-if="activeName==='shippingAddress'"></ShippingAddress>
        </el-tab-pane>
        <el-tab-pane label="商城订单" name="productOrder">
          <ProductOrder v-if="activeName==='productOrder'"></ProductOrder>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useRoute} from "vue-router";
import PetFosterCare from "@/views/front/personalCenter/PetFosterCare.vue";
import PetFeed from "@/views/front/personalCenter/PetFeed.vue";
import ShippingAddress from "@/views/front/personalCenter/ShippingAddress.vue";
import ProductOrder from "@/views/front/personalCenter/ProductOrder.vue";

const activeName = ref('petFosterCare')
const route = useRoute()
if (route.query.type) {
  activeName.value = route.query.type
}
</script>

<style>

</style>
