package com.project.platform.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.project.platform.dto.CurrentUserDTO;
import com.project.platform.dto.RetrievePasswordDTO;
import com.project.platform.dto.UpdatePasswordDTO;
import com.project.platform.entity.PetStoreManager;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetStoreManagerMapper;
import com.project.platform.service.PetStoreManagerService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 宠物店长
 */
@Service
public class PetStoreManagerServiceImpl implements PetStoreManagerService {
    @Resource
    private PetStoreManagerMapper petStoreManagerMapper;

    @Value("${resetPassword}")
    private String resetPassword;

    @Override
    public PageVO<PetStoreManager> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetStoreManager> page = new PageVO();
        List<PetStoreManager> list = petStoreManagerMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(petStoreManagerMapper.queryCount(query));
        return page;
    }

    @Override
    public PetStoreManager selectById(Integer id) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(id);
        return petStoreManager;
    }

    @Override
    public List<PetStoreManager> list() {
        return petStoreManagerMapper.list();
    }

    @Override
    public void insert(PetStoreManager entity) {
        check(entity);
        if (entity.getPassword() == null) {
            entity.setPassword(resetPassword);
        }
        petStoreManagerMapper.insert(entity);
    }

    @Override
    public void updateById(PetStoreManager entity) {
        check(entity);
        petStoreManagerMapper.updateById(entity);
    }

    private void check(PetStoreManager entity) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectByUsername(entity.getUsername());
        if (petStoreManager != null && petStoreManager.getId() != entity.getId()) {
            throw new CustomException("用户名已存在");
        }
        petStoreManager = petStoreManagerMapper.selectByStoreName(entity.getUsername());
        if (petStoreManager != null && petStoreManager.getId() != entity.getId()) {
            throw new CustomException("店铺名称已存在");
        }
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        petStoreManagerMapper.removeByIds(ids);
    }


    @Override
    public CurrentUserDTO login(String username, String password) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectByUsername(username);
        if (petStoreManager == null || !petStoreManager.getPassword().equals(password)) {
            throw new CustomException("用户名或密码错误");
        }
        if (petStoreManager.getStatus().equals("禁用")) {
            throw new CustomException("用户已禁用");
        }
        CurrentUserDTO currentUserDTO = new CurrentUserDTO();
        BeanUtils.copyProperties(petStoreManager, currentUserDTO);
        return currentUserDTO;
    }

    @Override
    public void register(JSONObject data) {
        PetStoreManager petStoreManager = new PetStoreManager();
        petStoreManager.setUsername(data.getString("username"));
        petStoreManager.setNickname(data.getString("nickname"));
        petStoreManager.setAvatarUrl(data.getString("avatarUrl"));
        petStoreManager.setPassword(data.getString("password"));
        petStoreManager.setStoreName(data.getString("storeName"));
        petStoreManager.setStoreImg(data.getString("storeImg"));
        petStoreManager.setStoreAddress(data.getString("storeAddress"));
        petStoreManager.setStatus("启用");
        insert(petStoreManager);
    }

    @Override
    public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(currentUserDTO.getId());
        petStoreManager.setId(currentUserDTO.getId());
        petStoreManager.setNickname(currentUserDTO.getNickname());
        petStoreManager.setAvatarUrl(currentUserDTO.getAvatarUrl());
        petStoreManager.setTel(currentUserDTO.getTel());
        petStoreManager.setEmail(currentUserDTO.getEmail());
        petStoreManagerMapper.updateById(petStoreManager);
    }

    @Override
    public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(CurrentUserThreadLocal.getCurrentUser().getId());
        if (!petStoreManager.getPassword().equals(updatePassword.getOldPassword())) {
            throw new CustomException("旧密码不正确");
        }
        petStoreManager.setPassword(updatePassword.getNewPassword());
        petStoreManagerMapper.updateById(petStoreManager);
    }

    @Override
    public void resetPassword(Integer id) {
        PetStoreManager petStoreManager = petStoreManagerMapper.selectById(id);
        petStoreManager.setPassword(resetPassword);
        petStoreManagerMapper.updateById(petStoreManager);
    }

    /**
     * 忘记密码
     *
     * @param retrievePasswordDTO
     */
    @Override
    public void retrievePassword(RetrievePasswordDTO retrievePasswordDTO) {

    }

}
