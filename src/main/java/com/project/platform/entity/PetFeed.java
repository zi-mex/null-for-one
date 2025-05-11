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


    public void setId(Integer id) {
      this.id = id;
   }

    public void setPetId(Integer petId) {
      this.petId = petId;
   }

    public void setPetName(String petName) {
      this.petName = petName;
   }

    public void setPetTypeId(Integer petTypeId) {
      this.petTypeId = petTypeId;
   }

    public void setPetTypeName(String petTypeName) {
      this.petTypeName = petTypeName;
   }

    public void setUserId(Integer userId) {
      this.userId = userId;
   }

    public void setUsername(String username) {
      this.username = username;
   }

    public void setPetStoreManagerId(Integer petStoreManagerId) {
      this.petStoreManagerId = petStoreManagerId;
   }

    public void setStoreName(String storeName) {
      this.storeName = storeName;
   }

    public void setReservedTime(LocalDateTime reservedTime) {
      this.reservedTime = reservedTime;
   }

    public void setRemark(String remark) {
      this.remark = remark;
   }

    public void setStatus(String status) {
      this.status = status;
   }

    public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
