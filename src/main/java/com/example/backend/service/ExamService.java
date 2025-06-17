package com.example.backend.service;

import com.example.backend.dto.ExamDTO;
import com.example.backend.dto.QuestionScoreDTO;
import com.example.backend.entity.Exam;
import com.example.backend.entity.ExamQuestion;
import com.example.backend.entity.Question;
import com.example.backend.repository.ExamRepository;
import com.example.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    public Exam createExamFromDTO(ExamDTO dto) {
        Exam exam = new Exam();
        exam.setTitle(dto.getTitle());
        exam.setDuration(dto.getDuration());
        exam.setTotalScore(dto.getTotalScore());

        List<ExamQuestion> examQuestions = dto.getQuestions().stream().map(q -> {
            Question question = questionRepository.findById(q.getId()).orElseThrow();
            ExamQuestion eq = new ExamQuestion();
            eq.setQuestion(question);
            eq.setScore(q.getScore());
            eq.setExam(exam);
            return eq;
        }).collect(Collectors.toList());

        exam.setQuestions(examQuestions);
        return examRepository.save(exam);
    }

    public Exam updateExamFromDTO(String id, ExamDTO dto) {
        Exam exam = examRepository.findById(id).orElseThrow();

        exam.setTitle(dto.getTitle());
        exam.setDuration(dto.getDuration());
        exam.setTotalScore(dto.getTotalScore());

        exam.getQuestions().clear();

        List<ExamQuestion> examQuestions = dto.getQuestions().stream().map(q -> {
            Question question = questionRepository.findById(q.getId()).orElseThrow();
            ExamQuestion eq = new ExamQuestion();
            eq.setQuestion(question);
            eq.setScore(q.getScore());
            eq.setExam(exam);
            return eq;
        }).collect(Collectors.toList());

        exam.getQuestions().addAll(examQuestions);
        return examRepository.save(exam);
    }
}
