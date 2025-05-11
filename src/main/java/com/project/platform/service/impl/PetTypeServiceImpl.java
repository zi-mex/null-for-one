package com.project.platform.service.impl;

import com.project.platform.entity.PetType;
import com.project.platform.mapper.PetTypeMapper;
import com.project.platform.service.PetTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * 宠物类型
 */
@Service
public class PetTypeServiceImpl  implements PetTypeService {
    @Resource
    private PetTypeMapper petTypeMapper;
    
    @Override
    public PageVO<PetType> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetType> page = new PageVO();
        List<PetType> list = petTypeMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petTypeMapper.queryCount(query));
        return page;
    }

    @Override
    public PetType selectById(Integer id) {
        PetType petType = petTypeMapper.selectById(id);
        return petType;
    }

    @Override
    public List<PetType> list() {
        return petTypeMapper.list();
    }
    @Override
    public void insert(PetType entity) {
        check(entity);
        petTypeMapper.insert(entity);
    }
    @Override
    public void updateById(PetType entity) {
        check(entity);
        petTypeMapper.updateById(entity);
    }
    private void check(PetType entity) {

    }
    @Override
    public void removeByIds(List<Integer> ids) {
        petTypeMapper.removeByIds(ids);
    }
}
