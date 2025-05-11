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
