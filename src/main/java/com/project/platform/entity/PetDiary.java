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
