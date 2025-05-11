package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 商品订单
 */
@Data
public class ProductOrder  {
   /**
    * id
    */
   private Integer id;
   /**
   * 商品ID
   */
   private Integer productId;
   /**
   * 商品名称
   */
   private String productName;
   /**
   * 用户ID
   */
   private Integer userId;
   /**
   * 用户名称
   */
   private String username;
   /**
   * 价格
   */
   private Float price;
   /**
    * 状态
    */
   private String status;
   /**
   * 收货人姓名
   */
   private String nameOfConsignee;
   /**
   * 收货人电话
   */
   private String telOfConsignee;
   /**
   * 收货人地址
   */
   private String addressOfConsignee;
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
   public Integer getProductId() {
      return productId;
   }

   public void setProductId(Integer productId) {
      this.productId = productId;
   }

   public String getProductName() {
      return productName;
   }

   public void setProductName(String productName) {
      this.productName = productName;
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

   public Float getPrice() {
      return price;
   }

   public void setPrice(Float price) {
      this.price = price;
   }

   public String getNameOfConsignee() {
      return nameOfConsignee;
   }

   public void setNameOfConsignee(String nameOfConsignee) {
      this.nameOfConsignee = nameOfConsignee;
   }

   public String getTelOfConsignee() {
      return telOfConsignee;
   }

   public void setTelOfConsignee(String telOfConsignee) {
      this.telOfConsignee = telOfConsignee;
   }

   public String getAddressOfConsignee() {
      return addressOfConsignee;
   }

   public void setAddressOfConsignee(String addressOfConsignee) {
      this.addressOfConsignee = addressOfConsignee;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
