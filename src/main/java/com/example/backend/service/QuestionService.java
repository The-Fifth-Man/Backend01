package com.example.backend.service;

import com.example.backend.entity.Question;
import com.example.backend.entity.QuestionType;
import com.example.backend.repository.QuestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class QuestionService {

    private final QuestionRepository repo;

    public QuestionService(QuestionRepository repo) {
        this.repo = repo;
    }

    /**
     * 分页 + 多条件筛选查询
     *
     * @param subject   学科筛选
     * @param type      题型筛选
     * @param difficulty 难度筛选
     * @param tag       标签筛选（模糊匹配 JSON）
     * @param search    关键词匹配 content 和 answer
     * @param page      页码，从 0 开始
     * @param size      每页大小
     */
    public Page<Question> query(
            String subject,
            QuestionType type,
            Integer difficulty,
            String tag,
            String search,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Question> spec = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Question> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (subject != null && !subject.isEmpty()) {
                    predicates.add(cb.equal(root.get("subject"), subject));
                }
                if (type != null) {
                    predicates.add(cb.equal(root.get("type"), type));
                }
                if (difficulty != null) {
                    predicates.add(cb.equal(root.get("difficulty"), difficulty));
                }
                if (tag != null && !tag.isEmpty()) {
                    predicates.add(cb.like(root.get("tags"), "%" + tag + "%"));
                }
                if (search != null && !search.isEmpty()) {
                    String like = "%" + search + "%";
                    predicates.add(cb.or(
                            cb.like(root.get("content"), like),
                            cb.like(root.get("answer"), like)
                    ));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        return repo.findAll(spec, pageable);
    }

    /**
     * 保存题目（新增或更新）
     */
    public Question save(Question q) {
        // 双向关联选项（如果有）
        if (q.getOptions() != null) {
            q.getOptions().forEach(opt -> opt.setQuestion(q));
        }
        System.out.println("保存题目: " + q);
        return repo.save(q);
    }

    /**
     * 删除题目
     */
    public void delete(String id) {
        repo.deleteById(id);
    }

    /**
     * 获取单个题目
     */
    public Question get(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));
    }

    /**
     * 获取所有学科
     */
    public List<String> getAllSubjects() {
        return repo.findDistinctSubjects();
    }

    /**
     * 获取所有标签（从 JSON 数组字段中提取唯一值）
     */
    public List<String> getAllTags() {
        List<String> rawTags = repo.findAllTagsRaw();
        ObjectMapper mapper = new ObjectMapper();
        Set<String> result = new HashSet<>();

        for (String tagJson : rawTags) {
            if (tagJson == null || tagJson.isEmpty()) continue;
            try {
                List<String> tags = mapper.readValue(tagJson, new TypeReference<List<String>>() {});
                result.addAll(tags);
            } catch (Exception e) {
                e.printStackTrace(); // 若解析失败则跳过该条
            }
        }
        return new ArrayList<>(result);
    }
}
