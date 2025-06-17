package com.example.backend.service;

import com.example.backend.entity.ExamSubmission;
import com.example.backend.repository.ExamSubmissionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class ExamSubmissionService {
    private final ExamSubmissionRepository repo;
    public ExamSubmissionService(ExamSubmissionRepository repo) { this.repo = repo; }

    public ExamSubmission submit(ExamSubmission sub) {
        sub.setSubmittedAt(Instant.now());
        return repo.save(sub);
    }

    public Page<ExamSubmission> history(String userId, int page, int size) {
        List<ExamSubmission> list = repo.findByUserId(userId);
        return new PageImpl<>(list, PageRequest.of(page, size), list.size());
    }

    public ExamSubmission get(String id) { return repo.findById(id).orElseThrow(); }
}