package com.project.platform.controller;


import com.project.platform.service.StatisticalReportFormsService;
import com.project.platform.vo.EchartsDataVO;
import com.project.platform.vo.ResponseVO;
import com.project.platform.vo.ValueNameVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statisticalReportForms")
public class StatisticalReportFormsController {

    @Resource
    private StatisticalReportFormsService statisticalReportFormsService;

    @GetMapping("productSalesTotalAmountChart/{day}")
    public ResponseVO<EchartsDataVO> getProductSalesTotalAmountChart(@PathVariable int day) {
        return ResponseVO.ok(statisticalReportFormsService.getProductSalesTotalAmountChart(day));
    }

    @GetMapping("petTypeProportionOfChar")
    public ResponseVO<List<ValueNameVO>> getPetTypeProportionOfChart() {
        return ResponseVO.ok(statisticalReportFormsService.getPetTypeProportionOfChart());
    }


}
