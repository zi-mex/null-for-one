package com.project.platform.mapper;

import com.project.platform.entity.ProductOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface ProductOrderMapper {
    List<ProductOrder> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM product_order WHERE id = #{id}")
    ProductOrder selectById(Integer id);

    @Select("SELECT * FROM product_order")
    List<ProductOrder> list();

    int insert(ProductOrder entity);

    int updateById(ProductOrder entity);

    boolean removeByIds(List<Integer> ids);

    /**
     * 查询最近已完成
     *
     * @param day
     * @return
     */
    @Select("SELECT * FROM product_order WHERE status='已完成' and  create_time >= DATE_SUB(NOW(), INTERVAL #{day} DAY) ")
    List<ProductOrder> selectRecentlyCompleted(Integer day);
}
