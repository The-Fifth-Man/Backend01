package com.example.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamDTO {
    private String title;
    private Integer duration;
    private Integer totalScore;
    private List<QuestionScoreDTO> questions;
}
