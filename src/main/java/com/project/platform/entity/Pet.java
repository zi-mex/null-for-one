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
