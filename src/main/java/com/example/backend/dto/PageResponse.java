package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页响应 DTO
 *
 * @param <T> 列表元素类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    /** 总记录数（用于分页） */
    private long total;

    /** 当前页数据列表 */
    private List<T> items;
}
