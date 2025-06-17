package com.example.backend.controller;

import com.example.backend.dto.ExamDTO;
import com.example.backend.entity.Exam;
import com.example.backend.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody ExamDTO dto) {
        Exam created = examService.createExamFromDTO(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable String id, @RequestBody ExamDTO dto) {
        Exam updated = examService.updateExamFromDTO(id, dto);
        return ResponseEntity.ok(updated);
    }
}
