package com.project.platform.service;

import com.project.platform.entity.PetStoreManager;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物店长
 */
public interface PetStoreManagerService extends CommonService {

    PageVO<PetStoreManager> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    PetStoreManager selectById(Integer id);

    List<PetStoreManager> list();

    void insert(PetStoreManager entity);

    void updateById(PetStoreManager entity);

    void removeByIds(List<Integer> id);
}
