package com.example.backend.service;

import com.example.backend.entity.Exam;
import com.example.backend.repository.ExamRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamService {
    private final ExamRepository repo;
    public ExamService(ExamRepository repo) { this.repo = repo; }

    public Page<Exam> query(String search, int page, int size) {
        List<Exam> list = repo.findAll();
        return new PageImpl<>(list, PageRequest.of(page, size), list.size());
    }
    public Exam save(Exam e) { return repo.save(e); }
    public void delete(String id) { repo.deleteById(id); }
    public Exam get(String id) { return repo.findById(id).orElseThrow(); }
}