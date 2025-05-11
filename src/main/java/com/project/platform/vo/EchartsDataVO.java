package com.project.platform.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EchartsDataVO {
    private List<Object> xData;
    private List<Object> yData;
    private List<Object> seriesData;


    public EchartsDataVO() {
        this.xData = new ArrayList<>();
        this.yData = new ArrayList<>();
        this.seriesData = new ArrayList<>();
    }
}

