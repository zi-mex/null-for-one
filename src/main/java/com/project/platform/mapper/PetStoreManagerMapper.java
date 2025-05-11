package com.project.platform.mapper;

import com.project.platform.entity.PetStoreManager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PetStoreManagerMapper {
    List<PetStoreManager> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM pet_store_manager WHERE id = #{id}")
    PetStoreManager selectById(Integer id);

    @Select("SELECT * FROM pet_store_manager")
    List<PetStoreManager> list();

    int insert(PetStoreManager entity);

    int updateById(PetStoreManager entity);

    boolean removeByIds(List<Integer> ids);

    @Select("SELECT * FROM pet_store_manager WHERE username = #{username}")
    PetStoreManager selectByUsername(String username);

    @Select("SELECT * FROM pet_store_manager WHERE store_name = #{storeName}")
    PetStoreManager selectByStoreName(String storeName);
}
