package com.project.platform.dto;

import lombok.Data;

@Data
public class CurrentUserDTO {
    private Integer id;
    private String type;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String tel;
    private String email;

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

}
