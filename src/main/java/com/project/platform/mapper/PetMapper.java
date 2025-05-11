package com.project.platform.mapper;

import com.project.platform.entity.Pet;
import com.project.platform.vo.ValueNameVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface PetMapper {
    List<Pet> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet WHERE id = #{id}")
    Pet selectById(Integer id);

    @Select("SELECT * FROM pet")
    List<Pet> list();

    int insert(Pet entity);

    int updateById(Pet entity);

    boolean removeByIds(List<Integer> ids);

    @Select("select pet_type.name  as name,pet.count  as value from  pet_type  join ( SELECT pet_type_id,COUNT(*) AS count FROM pet  GROUP BY pet_type_id)  pet on pet.pet_type_id=pet_type.id")
    List<ValueNameVO> selectTypeCount();
}
