package com.project.platform.utils;

import com.project.platform.dto.CurrentUserDTO;

public class CurrentUserThreadLocal {

    /**
     * 线程变量隔离
     */
    private static final ThreadLocal<CurrentUserDTO> CURRENT_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清除用户信息
     */
    public static void clear() {
        CURRENT_USER_THREAD_LOCAL.remove();
    }

    /**
     * 存储用户信息
     */
    public static void set(CurrentUserDTO currentUserDTO) {
        CURRENT_USER_THREAD_LOCAL.set(currentUserDTO);
    }

    /**
     * 获取当前用户信息
     */
    public static CurrentUserDTO getCurrentUser() {
        return CURRENT_USER_THREAD_LOCAL.get();
    }
}
