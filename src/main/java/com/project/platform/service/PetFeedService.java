package com.project.platform.service;

import com.project.platform.entity.PetFeed;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物喂养
 */
public interface PetFeedService {

    PageVO<PetFeed> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetFeed selectById(Integer id);

    List<PetFeed> list();

    void insert(PetFeed entity);

    void updateById(PetFeed entity);

    void removeByIds(List<Integer> id);

    void inService(Integer id);

    void finish(Integer id);
}
