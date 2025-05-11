package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 收货地址
 */
@Data
public class ShippingAddress  {
   /**
    * id
    */
   private Integer id;
   /**
   * 姓名
   */
   private String name;
   /**
   * 电话
   */
   private String tel;
   /**
   * 地址
   */
   private String address;
   /**
   * 用户ID
   */
   private Integer userId;
   /**
   * 用户名称
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

   public String getTel() {
      return tel;
   }

   public void setTel(String tel) {
      this.tel = tel;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
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
