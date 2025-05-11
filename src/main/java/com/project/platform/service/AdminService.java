package com.project.platform.service;

import com.project.platform.dto.CurrentUserDTO;
import com.project.platform.entity.Admin;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 */
public interface AdminService extends CommonService {

    PageVO<Admin> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    Admin selectById(Integer id);

    List<Admin> list();

    void insert(Admin entity);

    void updateById(Admin entity);

    void removeByIds(List<Integer> id);


}
