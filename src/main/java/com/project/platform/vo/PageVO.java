package com.project.platform.vo;


import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    List<T> list;
    long total;
}
