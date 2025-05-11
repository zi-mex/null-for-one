package com.project.platform.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.project.platform.dto.CurrentUserDTO;
import com.project.platform.dto.RetrievePasswordDTO;
import com.project.platform.dto.UpdatePasswordDTO;
import com.project.platform.entity.Admin;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.AdminMapper;
import com.project.platform.service.AdminService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;

    @Value("${resetPassword}")
    private String resetPassword;

    @Override
    public PageVO<Admin> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Admin> page = new PageVO();
        List<Admin> list = adminMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(adminMapper.queryCount(query));
        return page;
    }

    @Override
    public Admin selectById(Integer id) {
        Admin admin = adminMapper.selectById(id);
        return admin;
    }

    @Override
    public List<Admin> list() {
        return adminMapper.list();
    }

    @Override
    public void insert(Admin entity) {
        check(entity);
        if (entity.getPassword() == null) {
            entity.setPassword(resetPassword);
        }
        adminMapper.insert(entity);
    }

    @Override
    public void updateById(Admin entity) {
        check(entity);
        adminMapper.updateById(entity);
    }

    private void check(Admin entity) {
        Admin admin = adminMapper.selectByUserName(entity.getUsername());
        if (admin != null && admin.getId() != entity.getId()) {
            throw new CustomException("用户名已存在");
        }
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        adminMapper.removeByIds(ids);
    }

    @Override
    public CurrentUserDTO login(String username, String password) {
        Admin admin = adminMapper.selectByUserName(username);
        if (admin == null || !admin.getPassword().equals(password)) {
            throw new CustomException("用户名或密码错误");
        }
        if (admin.getStatus().equals("禁用")) {
            throw new CustomException("用户已禁用");
        }
        CurrentUserDTO currentUserDTO = new CurrentUserDTO();
        BeanUtils.copyProperties(admin, currentUserDTO);
        return currentUserDTO;
    }

    @Override
    public void register(JSONObject data) {
        Admin admin = new Admin();
        admin.setUsername(data.getString("username"));
        admin.setNickname(data.getString("nickname"));
        admin.setAvatarUrl(data.getString("avatarUrl"));
        admin.setPassword(data.getString("password"));
        admin.setStatus("启用");
        insert(admin);
    }


    @Override
    public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {
        Admin admin = adminMapper.selectById(currentUserDTO.getId());
        admin.setId(currentUserDTO.getId());
        admin.setNickname(currentUserDTO.getNickname());
        admin.setAvatarUrl(currentUserDTO.getAvatarUrl());
        admin.setTel(currentUserDTO.getTel());
        admin.setEmail(currentUserDTO.getEmail());
        adminMapper.updateById(admin);
    }

    @Override
    public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {
        Admin admin = adminMapper.selectById(CurrentUserThreadLocal.getCurrentUser().getId());
        if (!admin.getPassword().equals(updatePassword.getOldPassword())) {
            throw new CustomException("旧密码不正确");
        }
        admin.setPassword(updatePassword.getNewPassword());
        adminMapper.updateById(admin);
    }

    @Override
    public void resetPassword(Integer id) {
        Admin admin = adminMapper.selectById(id);
        admin.setPassword(resetPassword);
        adminMapper.updateById(admin);
    }

    @Override
    public void retrievePassword(RetrievePasswordDTO retrievePasswordDTO) {
        Admin admin = adminMapper.selectByTel(retrievePasswordDTO.getTel());
        if (admin == null) {
            throw new CustomException("手机号不存在");
        }
        //TODO 校验验证码
        admin.setPassword(retrievePasswordDTO.getPassword());
        adminMapper.updateById(admin);
    }


}
