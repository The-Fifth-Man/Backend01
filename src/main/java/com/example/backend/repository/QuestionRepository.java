package com.example.backend.repository;

import com.example.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 题目仓库，支持 CRUD 及动态筛选
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, String>, JpaSpecificationExecutor<Question> {

    /**
     * 获取所有不同的学科
     */
    @Query("SELECT DISTINCT q.subject FROM Question q")
    List<String> findDistinctSubjects();

    /**
     * 获取所有 tags 字符串（由前端去重和拆分）
     */
    @Query("SELECT q.tags FROM Question q WHERE q.tags IS NOT NULL")
    List<String> findAllTagsRaw();
}
