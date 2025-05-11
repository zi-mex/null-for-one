package com.project.platform.mapper;

import com.project.platform.entity.PetType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetTypeMapper {
    List<PetType> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_type WHERE id = #{id}")
    PetType selectById(Integer id);

    @Select("SELECT * FROM pet_type")
    List<PetType> list();

    int insert(PetType entity);

    int updateById(PetType entity);

    boolean removeByIds(List<Integer> ids);

}
