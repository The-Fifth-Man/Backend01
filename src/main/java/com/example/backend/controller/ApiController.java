package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.QuestionDTO;
import com.example.backend.entity.Option;
import com.example.backend.entity.Question;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final QuestionService qs;
    private final ExamService es;
    private final UserRepository ur;

    public ApiController(QuestionService qs, ExamService es,
                          UserRepository ur) {
        this.qs = qs;
        this.es = es;
        this.ur = ur;
    }

    // --- Questions ---
    @GetMapping("/questions")
    public ApiResponse<PageResponse<Question>> listQuestions(
            @RequestParam(required=false) String subject,
            @RequestParam(required=false) QuestionType type,
            @RequestParam(required=false) Integer difficulty,
            @RequestParam(required=false) String tag,
            @RequestParam(required=false) String search,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int pageSize) {
        Page<Question> p = qs.query(subject, type, difficulty, tag, search, page, pageSize);
        return ApiResponse.ok(new PageResponse<>(p.getTotalElements(), p.getContent()));
    }

    @GetMapping("/questions/{id}")
    public ApiResponse<Question> getQuestion(@PathVariable String id) {
        return ApiResponse.ok(qs.get(id));
    }

    @PostMapping("/questions")
    public ApiResponse<Question> createQuestion(@RequestBody QuestionDTO dto) {
        Question q = new Question();
        q.setSubject(dto.getSubject());
        q.setType(dto.getType());
        q.setDifficulty(dto.getDifficulty());
        q.setContent(dto.getContent());

        // 处理 tags：将 List<String> 转为 JSON 字符串
        try {
            q.setTags(new ObjectMapper().writeValueAsString(dto.getTags()));
        } catch (JsonProcessingException e) {
            return ApiResponse.fail(400, "标签格式错误");
        }

        // 处理 answer：单选为字符串，多选为 JSON 字符串
        if (dto.getAnswer() instanceof List<?>) {
            try {
                q.setAnswer(new ObjectMapper().writeValueAsString(dto.getAnswer()));
            } catch (JsonProcessingException e) {
                return ApiResponse.fail(400, "答案格式错误");
            }
        } else {
            q.setAnswer(String.valueOf(dto.getAnswer()));
        }

        // 设置选项
        List<Option> optionList = new ArrayList<>();
        for (QuestionDTO.OptionDTO optDto : dto.getOptions()) {
            Option opt = new Option();
            opt.setOptKey(optDto.getKey());
            opt.setValue(optDto.getValue());
            opt.setQuestion(q);  // 设置反向关系
            optionList.add(opt);
        }
        q.setOptions(optionList);

        return ApiResponse.ok(qs.save(q));
    }

    @PutMapping("/questions/{id}")
    public ApiResponse<Question> updateQuestion(@PathVariable String id, @RequestBody Question q) {
        q.setId(id);
        return ApiResponse.ok(qs.save(q));
    }

    @DeleteMapping("/questions/{id}")
    public ApiResponse<Void> deleteQuestion(@PathVariable String id) {
        qs.delete(id);
        return ApiResponse.ok(null);
    }


    // --- Meta ---
    @GetMapping("/subjects")
    public ApiResponse<List<String>> getSubjects() {
        return ApiResponse.ok(qs.getAllSubjects());
    }

    @GetMapping("/tags")
    public ApiResponse<List<String>> getTags() {
        return ApiResponse.ok(qs.getAllTags());
    }

    @GetMapping("/types")
    public ApiResponse<List<String>> getTypes() {
        return ApiResponse.ok(
                Arrays.stream(QuestionType.values())
                        .map(Enum::name)
                        .toList()
        );
    }
}