package com.project.platform.mapper;

import com.project.platform.entity.PetDiary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetDiaryMapper {
    List<PetDiary> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_diary WHERE id = #{id}")
    PetDiary selectById(Integer id);

    @Select("SELECT * FROM pet_diary")
    List<PetDiary> list();

    int insert(PetDiary entity);

    int updateById(PetDiary entity);

    boolean removeByIds(List<Integer> ids);
}
