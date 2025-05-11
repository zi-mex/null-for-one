package com.project.platform.mapper;

import com.project.platform.entity.ShippingAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface ShippingAddressMapper {
    List<ShippingAddress> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM shipping_address WHERE id = #{id}")
    ShippingAddress selectById(Integer id);

    @Select("SELECT * FROM shipping_address")
    List<ShippingAddress> list();

    int insert(ShippingAddress entity);

    int updateById(ShippingAddress entity);

    boolean removeByIds(List<Integer> ids);
}
