package com.project.platform.service;

import com.project.platform.entity.User;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface UserService extends CommonService{
    List<User> list();

    void insert(User entity);

    void updateById(User entity);

    void removeByIds(List<Integer> ids);

    PageVO<User> page(Map<String,Object> query, Integer pageNum, Integer pageSize);

    User selectById(Integer id);

    void topUp(Integer userId, Float amount);

    void consumption(Integer userId, Float amount);
}
