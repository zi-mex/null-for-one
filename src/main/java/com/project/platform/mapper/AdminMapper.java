package com.project.platform.mapper;


import com.project.platform.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface AdminMapper {
    List<Admin> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM admin WHERE id = #{id}")
    Admin selectById(Integer id);

    @Select("SELECT * FROM admin")
    List<Admin> list();

    int insert(Admin entity);

    int updateById(Admin entity);

    boolean removeByIds(List<Integer> ids);

    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin selectByUserName(String username);

    @Select("SELECT * FROM admin WHERE tel = #{tel}")
    Admin selectByTel(String tel);
}


