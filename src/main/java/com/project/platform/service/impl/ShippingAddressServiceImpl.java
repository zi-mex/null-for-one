package com.project.platform.service.impl;

import com.project.platform.entity.ShippingAddress;
import com.project.platform.mapper.ShippingAddressMapper;
import com.project.platform.service.ShippingAddressService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 收货地址
 */
@Service
public class ShippingAddressServiceImpl  implements ShippingAddressService {
    @Resource
    private ShippingAddressMapper shippingAddressMapper;
    
    @Override
    public PageVO<ShippingAddress> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<ShippingAddress> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<ShippingAddress> list = shippingAddressMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(shippingAddressMapper.queryCount(query));
        return page;
    }

    @Override
    public ShippingAddress selectById(Integer id) {
        ShippingAddress shippingAddress = shippingAddressMapper.selectById(id);
        return shippingAddress;
    }

    @Override
    public List<ShippingAddress> list() {
        return shippingAddressMapper.list();
    }
    @Override
    public void insert(ShippingAddress entity) {
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        shippingAddressMapper.insert(entity);
    }
    @Override
    public void updateById(ShippingAddress entity) {
        check(entity);
        shippingAddressMapper.updateById(entity);
    }
    private void check(ShippingAddress entity) {

    }
    @Override
    public void removeByIds(List<Integer> ids) {
        shippingAddressMapper.removeByIds(ids);
    }
}
