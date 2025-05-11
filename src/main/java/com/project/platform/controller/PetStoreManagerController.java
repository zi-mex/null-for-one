package com.project.platform.controller;

import com.project.platform.entity.PetStoreManager;
import com.project.platform.service.PetStoreManagerService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物店长
 */
@RestController
@RequestMapping("/petStoreManager")
public class PetStoreManagerController {
    @Resource
    private PetStoreManagerService petStoreManagerService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetStoreManager>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetStoreManager> page = petStoreManagerService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public  ResponseVO<PetStoreManager> selectById(@PathVariable("id") Integer id) {
        PetStoreManager entity = petStoreManagerService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetStoreManager>> list() {

return ResponseVO.ok(petStoreManagerService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetStoreManager entity) {
        petStoreManagerService.insert(entity);
return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetStoreManager entity) {
        petStoreManagerService.updateById(entity);
return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        petStoreManagerService.removeByIds(ids);
return ResponseVO.ok();
    }
}

