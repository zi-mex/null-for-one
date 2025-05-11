package com.project.platform.service;

import com.project.platform.entity.ProductOrder;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 商品订单
 */
public interface ProductOrderService {

    PageVO<ProductOrder> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    ProductOrder selectById(Integer id);

    List<ProductOrder> list();

    void insert(ProductOrder entity);

    void updateById(ProductOrder entity);

    void removeByIds(List<Integer> id);

    void pay(Integer id);

    void cancel(Integer id);

    void delivery(Integer id);

    void confirm(Integer id);
}
