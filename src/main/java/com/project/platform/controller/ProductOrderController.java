package com.project.platform.controller;

import com.project.platform.entity.ProductOrder;
import com.project.platform.service.ProductOrderService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品订单
 */
@RestController
@RequestMapping("/productOrder")
public class ProductOrderController {
    @Resource
    private ProductOrderService productOrderService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<ProductOrder>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<ProductOrder> page = productOrderService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public  ResponseVO<ProductOrder> selectById(@PathVariable("id") Integer id) {
        ProductOrder entity = productOrderService.selectById(id);
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<ProductOrder>> list() {
return ResponseVO.ok(productOrderService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody ProductOrder entity) {
        productOrderService.insert(entity);
return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody ProductOrder entity) {
        productOrderService.updateById(entity);
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
        productOrderService.removeByIds(ids);
return ResponseVO.ok();
    }

    /**
     * 支付
     *
     * @param id
     */
    @PostMapping("pay/{id}")
    public ResponseVO pay(@PathVariable("id") Integer id) {
        productOrderService.pay(id);
        return ResponseVO.ok();
    }

    /**
     * 取消
     *
     * @param id
     */

    @PostMapping("cancel/{id}")
    public  ResponseVO cancel(@PathVariable("id") Integer id) {
        productOrderService.cancel(id);
        return ResponseVO.ok();
    }

    /**
     * 发货
     *
     * @param id
     */
    @PostMapping("delivery/{id}")
    public  ResponseVO delivery(@PathVariable("id") Integer id) {
        productOrderService.delivery(id);
        return ResponseVO.ok();
    }

    /**
     * 确认收货
     *
     * @param id
     */
    @PostMapping("confirm/{id}")
    public  ResponseVO confirm(@PathVariable("id") Integer id) {
        productOrderService.confirm(id);
        return ResponseVO.ok();
    }
}
