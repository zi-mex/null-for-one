package com.project.platform.service;

import com.project.platform.entity.PetType;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物类型
 */
public interface PetTypeService {

    PageVO<PetType> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetType selectById(Integer id);

    List<PetType> list();

    void insert(PetType entity);

    void updateById(PetType entity);

    void removeByIds(List<Integer> id);
}
