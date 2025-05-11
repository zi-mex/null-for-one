package com.project.platform.mapper;

import com.project.platform.entity.PetFeed;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetFeedMapper {
    List<PetFeed> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_feed WHERE id = #{id}")
    PetFeed selectById(Integer id);

    @Select("SELECT * FROM pet_feed")
    List<PetFeed> list();

    int insert(PetFeed entity);

    int updateById(PetFeed entity);

    boolean removeByIds(List<Integer> ids);
}
