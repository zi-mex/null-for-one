package com.project.platform.service;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物寄养
 */
public interface PetFosterCareService {

    PageVO<PetFosterCare> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetFosterCare selectById(Integer id);

    List<PetFosterCare> list();

    void insert(PetFosterCare entity);

    void updateById(PetFosterCare entity);

    void removeByIds(List<Integer> id);

    void inService(Integer id);

    void finish(Integer id);
}
