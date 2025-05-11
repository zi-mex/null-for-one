package com.project.platform.service;

import com.project.platform.entity.Product;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 商品
 */
public interface ProductService {

    PageVO<Product> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    Product selectById(Integer id);

    List<Product> list();

    void insert(Product entity);

    void updateById(Product entity);

    void removeByIds(List<Integer> id);

    void out(Integer id);

    void in(Integer id);
}
