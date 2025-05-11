package com.project.platform.service.impl;

import com.project.platform.entity.Product;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.ProductMapper;
import com.project.platform.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public PageVO<Product> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Product> page = new PageVO();
        List<Product> list = productMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(productMapper.queryCount(query));
        return page;
    }

    @Override
    public Product selectById(Integer id) {
        Product product = productMapper.selectById(id);
        return product;
    }

    @Override
    public List<Product> list() {
        return productMapper.list();
    }

    @Override
    public void insert(Product entity) {
        entity.setSalesVolume(0);
        check(entity);
        productMapper.insert(entity);
    }

    @Override
    public void updateById(Product entity) {
        check(entity);
        productMapper.updateById(entity);
    }

    private void check(Product entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        productMapper.removeByIds(ids);
    }


    /**
     * 入库（退货）
     *
     * @param id
     */
    @Override
    public void in(Integer id) {
        Product product = productMapper.selectById(id);
        product.setStock(product.getStock() + 1);
        product.setSalesVolume(product.getSalesVolume() - 1);
        productMapper.updateById(product);
    }

    /**
     * 出库（售出）
     *
     * @param id
     */
    @Override
    public void out(Integer id) {
        Product product = productMapper.selectById(id);
        if (product.getStock() == 0) {
            throw new CustomException("库存不足");
        }
        product.setStock(product.getStock() - 1);
        product.setSalesVolume(product.getSalesVolume() + 1);
        productMapper.updateById(product);
    }
}
