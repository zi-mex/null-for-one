package com.project.platform.service;

import com.project.platform.entity.ShippingAddress;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 收货地址
 */
public interface ShippingAddressService {

    PageVO<ShippingAddress> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    ShippingAddress selectById(Integer id);

    List<ShippingAddress> list();

    void insert(ShippingAddress entity);

    void updateById(ShippingAddress entity);

    void removeByIds(List<Integer> id);
}
