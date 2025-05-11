package com.project.platform.service.impl;

import com.project.platform.entity.Pet;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetMapper;
import com.project.platform.service.PetService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物信息
 */
@Service
public class PetServiceImpl implements PetService {
    @Resource
    private PetMapper petMapper;

    @Override
    public PageVO<Pet> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Pet> page = new PageVO();
        //用户只能看到自己的宠物
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<Pet> list = petMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petMapper.queryCount(query));
        return page;
    }

    @Override
    public Pet selectById(Integer id) {
        Pet pet = petMapper.selectById(id);
        return pet;
    }

    @Override
    public List<Pet> list() {
        return petMapper.list();
    }

    @Override
    public void insert(Pet entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        petMapper.insert(entity);
    }

    @Override
    public void updateById(Pet entity) {
        check(entity);
        petMapper.updateById(entity);
    }

    private void check(Pet entity) {

    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petMapper.removeByIds(ids);
    }
}
