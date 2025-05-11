package com.project.platform.service.impl;

import com.project.platform.entity.PetFeed;
import com.project.platform.entity.PetFosterCare;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetFosterCareMapper;
import com.project.platform.service.PetFosterCareService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物寄养
 */
@Service
public class PetFosterCareServiceImpl implements PetFosterCareService {
    @Resource
    private PetFosterCareMapper petFosterCareMapper;

    @Override
    public PageVO<PetFosterCare> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetFosterCare> page = new PageVO();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        } else if (CurrentUserThreadLocal.getCurrentUser().getType().equals("PET_STORE_MANAGER")) {
            query.put("petStoreManagerId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<PetFosterCare> list = petFosterCareMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petFosterCareMapper.queryCount(query));
        return page;
    }

    @Override
    public PetFosterCare selectById(Integer id) {
        PetFosterCare petFosterCare = petFosterCareMapper.selectById(id);
        return petFosterCare;
    }

    @Override
    public List<PetFosterCare> list() {
        return petFosterCareMapper.list();
    }

    @Override
    public void insert(PetFosterCare entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setStatus("已下单");
        check(entity);
        petFosterCareMapper.insert(entity);
    }

    @Override
    public void updateById(PetFosterCare entity) {
        check(entity);
        petFosterCareMapper.updateById(entity);
    }

    private void check(PetFosterCare entity) {
        if (entity.getReservedStartTime().isAfter(entity.getReservedEndTime())) {
            throw new CustomException("开始时间不能大于结束时间");
        }

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petFosterCareMapper.removeByIds(ids);
    }

    @Override
    public void inService(Integer id) {
        PetFosterCare entity = petFosterCareMapper.selectById(id);
        entity.setStatus("服务中");
        petFosterCareMapper.updateById(entity);
    }

    @Override
    public void finish(Integer id) {
        PetFosterCare entity = petFosterCareMapper.selectById(id);
        entity.setStatus("已完成");
        petFosterCareMapper.updateById(entity);
    }
}
