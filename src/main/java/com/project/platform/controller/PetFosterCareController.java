package com.project.platform.controller;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.service.PetFosterCareService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物寄养
 */
@RestController
@RequestMapping("/petFosterCare")
public class PetFosterCareController {
    @Resource
    private PetFosterCareService petFosterCareService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<PetFosterCare>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetFosterCare> page = petFosterCareService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public  ResponseVO<PetFosterCare> selectById(@PathVariable("id") Integer id) {
        PetFosterCare entity = petFosterCareService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<PetFosterCare>> list() {
return ResponseVO.ok(petFosterCareService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetFosterCare entity) {
        petFosterCareService.insert(entity);
return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetFosterCare entity) {
        petFosterCareService.updateById(entity);
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
        petFosterCareService.removeByIds(ids);
return ResponseVO.ok();
    }

    @PostMapping("inService/{id}")
    public ResponseVO inService(@PathVariable("id") Integer id) {
        petFosterCareService.inService(id);
        return ResponseVO.ok();

    }

    @PostMapping("finish/{id}")
    public ResponseVO finish(@PathVariable("id") Integer id) {
        petFosterCareService.finish(id);
        return ResponseVO.ok();
    }
}
