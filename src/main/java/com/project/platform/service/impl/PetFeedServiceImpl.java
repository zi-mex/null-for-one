package com.project.platform.service.impl;

import com.project.platform.entity.PetFeed;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetFeedMapper;
import com.project.platform.service.PetFeedService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物喂养
 */
@Service
public class PetFeedServiceImpl implements PetFeedService {
    @Resource
    private PetFeedMapper petFeedMapper;

    @Override
    public PageVO<PetFeed> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetFeed> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        } else if (CurrentUserThreadLocal.getCurrentUser().getType().equals("PET_STORE_MANAGER")) {
            query.put("petStoreManagerId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<PetFeed> list = petFeedMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petFeedMapper.queryCount(query));
        return page;
    }

    @Override
    public PetFeed selectById(Integer id) {
        PetFeed petFeed = petFeedMapper.selectById(id);
        return petFeed;
    }

    @Override
    public List<PetFeed> list() {
        return petFeedMapper.list();
    }

    @Override
    public void insert(PetFeed entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setStatus("已下单");
        check(entity);
        petFeedMapper.insert(entity);
    }

    @Override
    public void updateById(PetFeed entity) {
        check(entity);
        petFeedMapper.updateById(entity);
    }

    private void check(PetFeed entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petFeedMapper.removeByIds(ids);
    }


    @Override
    public void inService(Integer id) {
        PetFeed entity = petFeedMapper.selectById(id);
        entity.setStatus("服务中");
        petFeedMapper.updateById(entity);
    }

    @Override
    public void finish(Integer id) {
        PetFeed entity = petFeedMapper.selectById(id);
        entity.setStatus("已完成");
        petFeedMapper.updateById(entity);
    }
}
