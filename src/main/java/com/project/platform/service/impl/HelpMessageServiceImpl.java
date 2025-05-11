package com.project.platform.service.impl;

import com.project.platform.entity.HelpMessage;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.HelpMessageMapper;
import com.project.platform.service.HelpMessageService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HelpMessageServiceImpl implements HelpMessageService {

    @Resource
    private HelpMessageMapper helpMessageMapper;

    @Override
    public PageVO<HelpMessage> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<HelpMessage> page = new PageVO<>();
        //判断是看自己还是看所有人
        if(query.containsKey("onlyYou")&&Boolean.valueOf(query.get("onlyYou").toString())){
            Integer userId =-1;
            if(CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
                //普通用户看自己的
                userId = CurrentUserThreadLocal.getCurrentUser().getId();
            }
            query.put("userId",userId);
        }
        //执行分页查询操作
        List<HelpMessage> list = helpMessageMapper.queryPage((pageNum - 1) * pageSize,pageSize,query);
        page.setList(list);
        //获取总条数
        page.setTotal(helpMessageMapper.queryCount(query));
        return page;
    }

    @Override
    public HelpMessage selectById(Integer id) {
        HelpMessage helpMessage = helpMessageMapper.selectById(id);
        return helpMessage;
    }

    @Override
    public List<HelpMessage> list() {
        return helpMessageMapper.list();
    }

    @Override
    public void insert(HelpMessage entity) {
        //求助模块一般由用户进行操作，管理员只负责管理
        if(!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            throw new CustomException("普通用户才允许添加");
        }

        //插入的时候如果有userId，就要在这里进行操作
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        helpMessageMapper.insert(entity);
    }

    @Override
    public void updateById(HelpMessage entity) {
        check(entity);
        helpMessageMapper.updateById(entity);
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        helpMessageMapper.removeByIds(ids);
    }

    public void check(HelpMessage entity){

    }
}
