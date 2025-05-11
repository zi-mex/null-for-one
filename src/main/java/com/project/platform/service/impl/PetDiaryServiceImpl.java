package com.project.platform.service.impl;

import com.project.platform.entity.PetDiary;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetDiaryMapper;
import com.project.platform.service.PetDiaryService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物日记
 */
@Service
public class PetDiaryServiceImpl implements PetDiaryService {
    @Resource
    private PetDiaryMapper petDiaryMapper;

    @Override
    public PageVO<PetDiary> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetDiary> page = new PageVO();
        if (query.containsKey("onlyYou") && Boolean.valueOf(query.get("onlyYou").toString())) {
            Integer userId = -1;
            if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
                //普通用户看自己才有效果
                userId = CurrentUserThreadLocal.getCurrentUser().getId();
            }
            query.put("userId", userId);
        }
        List<PetDiary> list = petDiaryMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petDiaryMapper.queryCount(query));
        return page;
    }

    @Override
    public PetDiary selectById(Integer id) {
        PetDiary petDiary = petDiaryMapper.selectById(id);
        return petDiary;
    }

    @Override
    public List<PetDiary> list() {
        return petDiaryMapper.list();
    }

    @Override
    public void insert(PetDiary entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        petDiaryMapper.insert(entity);
    }

    @Override
    public void updateById(PetDiary entity) {
        check(entity);
        petDiaryMapper.updateById(entity);
    }

    private void check(PetDiary entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petDiaryMapper.removeByIds(ids);
    }
}
