package com.project.platform.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.project.platform.dto.CurrentUserDTO;
import com.project.platform.dto.RetrievePasswordDTO;
import com.project.platform.dto.UpdatePasswordDTO;
import com.project.platform.entity.User;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.UserMapper;
import com.project.platform.service.UserService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service //表示在service层，为一个服务
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Value("${resetPassword}")
    private String resetPassword;
    @Autowired
    private UserService userService;

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public void insert(User entity) {
        //需求：当前用户名不能重复
        check(entity);
        //password为null时，设置密码为123456
        if(entity.getPassword() == null ){
            entity.setPassword(resetPassword);
        }
        userMapper.insert(entity);
    }

    @Override
    public void updateById(User entity) {
        check(entity);
        userMapper.updateById(entity);
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        userMapper.removeByIds(ids);
    }

    @Override
    public PageVO<User> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<User> page = new PageVO<>();
        //通过查询条件获取列表对象
        List<User> list = userMapper.queryPage((pageNum-1)*pageSize,pageSize,query);
        page.setList(list);
        //通过查询条件获取总数
        page.setTotal(userMapper.queryCount((query)));
        return page;
    }

    /**
     * 判断用户是否重复
     * @param entity
     */
    private void check(User entity) {
        User user = userMapper.selectByUsername(entity.getUsername());
        if (user != null && user.getId() != entity.getId()) {
            throw new CustomException("用户名已存在");
        }
    }

    @Override
    public CurrentUserDTO login(String username, String password) {
        //查询用户名是否存在
        User user = userMapper.selectByUsername(username);
        if (user == null||!password.equals(user.getPassword())) {
            throw new CustomException("用户名或密码错误");
        }

        //禁用
        if("禁用".equals(user.getStatus())){
            throw new CustomException("用户名已禁用");
        }

        CurrentUserDTO currentUserDTO = new CurrentUserDTO();
        BeanUtils.copyProperties(user, currentUserDTO);
        return currentUserDTO;
    }

    @Override
    public void register(JSONObject data) {
        User user = new User();
        user.setUsername(data.getString("username"));
        user.setNickname(data.getString("nickname"));
        user.setAvatarUrl(data.getString("avatarUrl"));
        user.setPassword(data.getString("password"));
        //新用户赠送100 余额
        user.setBalance(100F);
        user.setStatus("启用");
        insert(user);
    }

    @Override
    public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {
        User user = userMapper.selectById(currentUserDTO.getId());
        user.setId(currentUserDTO.getId());
        user.setNickname(currentUserDTO.getNickname());
        user.setAvatarUrl(currentUserDTO.getAvatarUrl());
        user.setTel(currentUserDTO.getTel());
        user.setEmail(currentUserDTO.getEmail());
        userMapper.updateById(user);
    }

    @Override
    public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {
        User user = userMapper.selectById(CurrentUserThreadLocal.getCurrentUser().getId());
        if(!user.getPassword().equals(updatePassword.getOldPassword())){
            throw new CustomException("旧密码不正确");
        }
        user.setPassword(updatePassword.getNewPassword());
        userMapper.updateById(user);
    }

    @Override
    public void resetPassword(Integer id) {
        User user = userMapper.selectById(id);
        user.setPassword(resetPassword);
        userMapper.updateById(user);
    }

    @Override
    public void retrievePassword(RetrievePasswordDTO retrievePasswordDTO) {

    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 充值
     *
     * @param userId
     * @param amount
     */

    public void topUp(Integer userId, Float amount) {
        User user = selectById(userId);
        user.setBalance(user.getBalance() + amount);
        userMapper.updateById(user);
    }

    /**
     * 消费
     *
     * @param userId
     * @param amount
     */
    public void consumption(Integer userId, Float amount) {
        User user = selectById(userId);
        user.setBalance(user.getBalance() - amount);
        if (user.getBalance() < 0) {
            throw new CustomException("余额不足");
        }
        userMapper.updateById(user);
    }

}
