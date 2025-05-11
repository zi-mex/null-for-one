package com.project.platform.controller;

import com.alibaba.fastjson2.JSONObject;
import com.project.platform.dto.CurrentUserDTO;
import com.project.platform.dto.LoginDTO;
import com.project.platform.dto.RetrievePasswordDTO;
import com.project.platform.dto.UpdatePasswordDTO;
import com.project.platform.exception.CustomException;
import com.project.platform.service.AdminService;
import com.project.platform.service.CommonService;
import com.project.platform.service.PetStoreManagerService;
import com.project.platform.service.UserService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.utils.JwtUtils;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通用
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;

    @Resource
    private PetStoreManagerService petStoreManagerService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("login")
    public ResponseVO<String> login(@RequestBody LoginDTO loginDTO) {
        CommonService commonService = getCommonService(loginDTO.getType());
        CurrentUserDTO currentUserDTO = commonService.login(loginDTO.getUsername(), loginDTO.getPassword());
        currentUserDTO.setType(loginDTO.getType());
        String token = JwtUtils.generateToken(currentUserDTO);
        return ResponseVO.ok(token);
    }

    /**
     * 注册
     *
     * @param data
     */
    @PutMapping("register")
    public ResponseVO register(@RequestBody JSONObject data) {
        String type = data.getString("type");
        CommonService commonService = getCommonService(type);
        commonService.register(data);
        return ResponseVO.ok();
    }

    /**
     * 修改当前用户信息
     *
     * @param currentUserDTO
     */

    @PostMapping("updateCurrentUser")
    public ResponseVO updateCurrentUser(@RequestBody CurrentUserDTO currentUserDTO) {
        CommonService commonService = getCommonService(CurrentUserThreadLocal.getCurrentUser().getType());
        commonService.updateCurrentUserInfo(currentUserDTO);
        return ResponseVO.ok();
    }

    /**
     * 修改密码
     *
     * @param updatePassword
     */

    @PostMapping("updatePassword")
    public ResponseVO updatePassword(@RequestBody UpdatePasswordDTO updatePassword) {
        CommonService commonService = getCommonService(CurrentUserThreadLocal.getCurrentUser().getType());
        commonService.updateCurrentUserPassword(updatePassword);
        return ResponseVO.ok();
    }

    /**
     * 忘记密码
     * @param retrievePasswordDTO
     * @return
     */

    @PostMapping("retrievePassword")
    public ResponseVO retrievePassword(@RequestBody RetrievePasswordDTO retrievePasswordDTO) {
        CommonService commonService = getCommonService(retrievePasswordDTO.getType());
        commonService.retrievePassword(retrievePasswordDTO);
        return ResponseVO.ok();
    }

    /**
     * 重置密码
     *
     * @param type
     * @param id
     */

    @PostMapping("resetPassword")
    public ResponseVO resetPassword(@RequestParam String type, @RequestParam Integer id) {
        CommonService commonService = getCommonService(type);
        commonService.resetPassword(id);
        return ResponseVO.ok();
    }


    /**
     * 获取当前用户
     *
     * @return
     */
    @GetMapping("currentUser")
    public ResponseVO<CurrentUserDTO> getCurrentUser() {
        Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
        CommonService commonService = getCommonService(CurrentUserThreadLocal.getCurrentUser().getType());
        CurrentUserDTO currentUserDTO = new CurrentUserDTO();
        BeanUtils.copyProperties(commonService.selectById(userId), currentUserDTO);
        currentUserDTO.setType(CurrentUserThreadLocal.getCurrentUser().getType());
        return ResponseVO.ok(currentUserDTO);
    }

    /**
     * 根据类型获取对应service
     *
     * @param type
     * @return
     */

    private CommonService getCommonService(String type) {
        switch (type) {
            case "ADMIN":
                return adminService;
            case "USER":
                return userService;
            case "PET_STORE_MANAGER":
                return petStoreManagerService;
            default:
                throw new CustomException("用户类型错误");
        }
    }
}
