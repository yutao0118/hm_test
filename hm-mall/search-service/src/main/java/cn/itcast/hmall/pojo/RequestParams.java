package cn.itcast.hmall.pojo;

import lombok.Data;

/**
 * 接收请求携带的参数
 */
@Data
public class RequestParams {
    private String key; // 搜索的关键字
    private Integer page; // 当前页页码
    private Integer size; // 每页显示条数
    private String sortBy; // 排序字段(暂时不需要)
    // 下面是新增的过滤条件参数
    private String brand;
    private String category;
    private Integer minPrice;
    private Integer maxPrice;
}