package com.project.platform.service;

import com.project.platform.entity.Pet;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物信息
 */
public interface PetService {

    PageVO<Pet> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    Pet selectById(Integer id);

    List<Pet> list();

    void insert(Pet entity);

    void updateById(Pet entity);

    void removeByIds(List<Integer> id);
}
