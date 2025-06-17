package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用接口响应结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;         // 状态码，如 200 成功，400 失败
    private T data;           // 数据体
    private String message;   // 提示消息，可为 null

    /**
     * 成功响应（默认 200）
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, data, "OK");
    }

    /**
     * 自定义成功响应
     */
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(200, data, message);
    }

    /**
     * 错误响应（默认 400）
     */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(400, null, message);
    }

    /**
     * 自定义错误响应
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, null, message);
    }
}
