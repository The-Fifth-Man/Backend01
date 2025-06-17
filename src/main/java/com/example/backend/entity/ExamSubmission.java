package com.example.backend.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Exam exam;
    @ManyToOne
    private User user;

    private Integer totalScore;
    private Integer score;
    private Integer totalCount;
    private Integer correctCount;
    private Long usedTime;

    @Type(value = JsonType.class)
    @Column(columnDefinition = "json")
    private String answers;

    private Instant submittedAt;
}