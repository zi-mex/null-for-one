package com.project.platform.service;


import com.project.platform.vo.EchartsDataVO;
import com.project.platform.vo.ValueNameVO;

import java.util.List;

public interface StatisticalReportFormsService {
    /**
     * 宠物类型占比
     * @return
     */
    List<ValueNameVO> getPetTypeProportionOfChart();


    /**
     * 销售总额统计
     *
     * @param day
     */
    EchartsDataVO getProductSalesTotalAmountChart(int day);
}
