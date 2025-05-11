package com.project.platform.controller;

import com.project.platform.entity.HelpMessage;
import com.project.platform.service.HelpMessageService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 求助信息
 */
@RestController
@RequestMapping("/helpMessage")
public class HelpMessageController {

    @Resource
    private HelpMessageService helpMessageService;

    /**
     * 分页查询
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<HelpMessage>> page(
            @RequestParam Map<String,Object> query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize

    ){
        PageVO<HelpMessage> page = helpMessageService.page(query,pageNum,pageSize);
        return ResponseVO.ok(page);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<HelpMessage> selectById(@PathVariable("id") Integer id){
        HelpMessage entity = helpMessageService.selectById(id);
        return ResponseVO.ok(entity);
    }

    /**
     * 列表
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<HelpMessage>> list(){
        return ResponseVO.ok(helpMessageService.list());
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody HelpMessage entity){
        helpMessageService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody HelpMessage entity){
        helpMessageService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids){
        helpMessageService.removeByIds(ids);
        return ResponseVO.ok();
    }

}
