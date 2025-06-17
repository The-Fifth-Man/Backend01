package com.example.backend.repository;

import com.example.backend.entity.ExamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, String> {
    List<ExamSubmission> findByUserId(String userId);
}