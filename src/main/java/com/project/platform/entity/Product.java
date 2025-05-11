package com.project.platform.entity;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * 商品
 */
@Data
public class Product  {
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
   * 介绍
   */
   private String introduce;
   /**
   * 状态
   */
   private String status;
   /**
   * 价格
   */
   private Float price;
   /**
   * 库存
   */
   private Integer stock;
   /**
   * 销量
   */
   private Integer salesVolume;
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

   public String getIntroduce() {
      return introduce;
   }

   public void setIntroduce(String introduce) {
      this.introduce = introduce;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public Float getPrice() {
      return price;
   }

   public void setPrice(Float price) {
      this.price = price;
   }

   public Integer getStock() {
      return stock;
   }

   public void setStock(Integer stock) {
      this.stock = stock;
   }

   public Integer getSalesVolume() {
      return salesVolume;
   }

   public void setSalesVolume(Integer salesVolume) {
      this.salesVolume = salesVolume;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }


}
