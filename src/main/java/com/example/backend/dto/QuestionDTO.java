// src/main/java/com/example/backend/dto/QuestionDTO.java
package com.example.backend.dto;

import com.example.backend.entity.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private String subject;
    private QuestionType type;
    private Integer difficulty;
    private String content;
    private Object answer; // 单选为字符串，多选为数组
    private List<String> tags;
    private List<OptionDTO> options;

    @Data
    public static class OptionDTO {
        private String key;
        private String value;
    }
}
