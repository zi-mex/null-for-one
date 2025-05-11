package com.project.platform.controller;

import com.project.platform.entity.User;
import com.project.platform.service.UserService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController  //相当于@Response（将当前类的返回体以json返回）+@Controller(表面当前类是控制器)
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    //前端 controller->service->mapper->xml 发起请求
    //后端 xml->mapper->service->controller 响应

    /**
     * 列表
     * @return
     */
    @GetMapping("list")  //以get方式请求
    public ResponseVO<List<User>> list(){
        return ResponseVO.ok(userService.list());
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody User entity){
        userService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody User entity){
        userService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 单选/批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids){
        userService.removeByIds(ids);
        return ResponseVO.ok();
    }

    /**
     * 分页
     * @param query 查询对象
     * @param pageNum 起始页，默认为1
     * @param pageSize 分页大小，默认为10
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<User>> page(
            @RequestParam Map<String ,Object> query,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize
            ){
        //调用service中的方法
        PageVO<User> page = userService.page(query,pageNum,pageSize);
        return ResponseVO.ok(page);
    }

    /**
     * 通过id查询数据
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<User> selectById(@PathVariable("id") Integer id){
        User user = userService.selectById(id);
        return ResponseVO.ok(user);
    }

    @PostMapping("/topUp/{amount}")
    public ResponseVO topUp(@PathVariable Float amount) {
        Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
        userService.topUp(userId, amount);
        return ResponseVO.ok();
    }

}
