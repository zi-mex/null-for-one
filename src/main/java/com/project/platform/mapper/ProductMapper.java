package com.project.platform.mapper;

import com.project.platform.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface ProductMapper {
    List<Product> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product selectById(Integer id);

    @Select("SELECT * FROM product")
    List<Product> list();

    int insert(Product entity);

    int updateById(Product entity);

    boolean removeByIds(List<Integer> ids);
}
