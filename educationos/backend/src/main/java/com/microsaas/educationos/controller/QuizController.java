package com.microsaas.educationos.controller;

import com.microsaas.educationos.domain.entity.Quiz;
import com.microsaas.educationos.dto.QuizSubmissionDto;
import com.microsaas.educationos.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/modules/{moduleId}/quizzes")
    public ResponseEntity<List<Quiz>> getQuizzes(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(quizService.getQuizzesForModule(moduleId));
    }

    @PostMapping("/modules/{moduleId}/quizzes/generate")
    public ResponseEntity<Quiz> generateQuiz(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(quizService.generateQuizForModule(moduleId));
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public ResponseEntity<BigDecimal> submitQuiz(
            @PathVariable UUID quizId, 
            @RequestParam UUID userId,
            @RequestBody QuizSubmissionDto submission) {
        return ResponseEntity.ok(quizService.submitQuiz(quizId, userId, submission));
    }
}
