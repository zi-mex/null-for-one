package com.project.platform.service.impl;

import com.project.platform.entity.Product;
import com.project.platform.entity.ProductOrder;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.ProductMapper;
import com.project.platform.mapper.ProductOrderMapper;
import com.project.platform.service.ProductOrderService;
import com.project.platform.service.ProductService;
import com.project.platform.service.UserService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 商品订单
 */
@Service
public class ProductOrderServiceImpl implements ProductOrderService {
    @Resource
    private ProductOrderMapper productOrderMapper;
    @Resource
    private ProductService productService;

    @Resource
    private UserService userService;

    @Override
    public PageVO<ProductOrder> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<ProductOrder> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<ProductOrder> list = productOrderMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(productOrderMapper.queryCount(query));
        return page;
    }

    @Override
    public ProductOrder selectById(Integer id) {
        ProductOrder productOrder = productOrderMapper.selectById(id);
        return productOrder;
    }

    @Override
    public List<ProductOrder> list() {
        return productOrderMapper.list();
    }

    @Override
    public void insert(ProductOrder entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许下单");
        }
        entity.setStatus("待支付");
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        Product product = productService.selectById(entity.getProductId());
        entity.setPrice(product.getPrice());
        productService.out(entity.getProductId());
        productOrderMapper.insert(entity);
    }

    @Override
    public void updateById(ProductOrder entity) {
        productOrderMapper.updateById(entity);
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        productOrderMapper.removeByIds(ids);
    }


    /**
     * 付款
     *
     * @param id
     */
    public void pay(Integer id) {
        ProductOrder productOrder = selectById(id);
        if (!productOrder.getStatus().equals("待支付")) {
            throw new CustomException("数据已过期，请先刷新页面");
        }
        //扣除用户余额
        userService.consumption(productOrder.getUserId(), productOrder.getPrice());
        productOrder.setStatus("待发货");
        productOrderMapper.updateById(productOrder);
    }

    /**
     * 取消
     *
     * @param id
     */
    public void cancel(Integer id) {
        ProductOrder productOrder = selectById(id);
        if (!productOrder.getStatus().equals("待支付")) {
            throw new CustomException("数据已过期，请先刷新页面");
        }
        //返回用户余额
        userService.topUp(productOrder.getUserId(), productOrder.getPrice());
        //返回库存
        productService.in(productOrder.getProductId());
        productOrder.setStatus("已取消");
        productOrderMapper.updateById(productOrder);
    }

    /**
     * 发货
     *
     * @param id
     */
    public void delivery(Integer id) {
        ProductOrder productOrder = selectById(id);
        if (!productOrder.getStatus().equals("待发货")) {
            throw new CustomException("数据已过期，请先刷新页面");
        }
        productOrder.setStatus("待收货");
        productOrderMapper.updateById(productOrder);
    }

    public void confirm(Integer id) {
        ProductOrder productOrder = selectById(id);
        if (!productOrder.getStatus().equals("待收货")) {
            throw new CustomException("数据已过期，请先刷新页面");
        }
        productOrder.setStatus("已完成");
        productOrderMapper.updateById(productOrder);
    }

}
