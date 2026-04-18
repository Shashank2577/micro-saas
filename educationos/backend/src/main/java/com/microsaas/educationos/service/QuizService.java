package com.microsaas.educationos.service;

import com.microsaas.educationos.domain.entity.Module;
import com.microsaas.educationos.domain.entity.Question;
import com.microsaas.educationos.domain.entity.Quiz;
import com.microsaas.educationos.domain.entity.ProgressRecord;
import com.microsaas.educationos.domain.entity.LearnerProfile;
import com.microsaas.educationos.domain.repository.ModuleRepository;
import com.microsaas.educationos.domain.repository.QuizRepository;
import com.microsaas.educationos.domain.repository.QuestionRepository;
import com.microsaas.educationos.domain.repository.ProgressRecordRepository;
import com.microsaas.educationos.domain.repository.LearnerProfileRepository;
import com.microsaas.educationos.dto.QuizSubmissionDto;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModuleRepository moduleRepository;
    
    @Autowired
    private ProgressRecordRepository progressRecordRepository;

    @Autowired
    private LearnerProfileRepository learnerProfileRepository;

    public List<Quiz> getQuizzesForModule(UUID moduleId) {
        UUID tenantId = TenantContext.require();
        return quizRepository.findByModuleIdAndTenantId(moduleId, tenantId);
    }

    public Quiz generateQuizForModule(UUID moduleId) {
        UUID tenantId = TenantContext.require();
        Module module = moduleRepository.findByIdAndTenantId(moduleId, tenantId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Simulate AI generation by creating a dummy quiz based on the module title
        Quiz quiz = new Quiz();
        quiz.setTenantId(tenantId);
        quiz.setModule(module);
        quiz.setTitle("Quiz on " + module.getTitle());
        quiz = quizRepository.save(quiz);

        Question q1 = new Question();
        q1.setTenantId(tenantId);
        q1.setQuiz(quiz);
        q1.setQuestionText("What is the main topic of " + module.getTitle() + "?");
        q1.setOptions("[\"Option A\", \"Option B\", \"Option C\", \"Option D\"]");
        q1.setCorrectOptionIndex(0);
        questionRepository.save(q1);

        return quiz;
    }

    public BigDecimal submitQuiz(UUID quizId, UUID userId, QuizSubmissionDto submission) {
        UUID tenantId = TenantContext.require();
        Quiz quiz = quizRepository.findByIdAndTenantId(quizId, tenantId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        LearnerProfile profile = learnerProfileRepository.findByUserIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new RuntimeException("Learner profile not found"));

        List<Question> questions = questionRepository.findByQuizIdAndTenantId(quizId, tenantId);
        
        int correctCount = 0;
        for (Question q : questions) {
            Integer submittedAnswer = submission.getAnswers().get(q.getId());
            if (submittedAnswer != null && submittedAnswer.equals(q.getCorrectOptionIndex())) {
                correctCount++;
            }
        }
        
        BigDecimal score = questions.isEmpty() ? BigDecimal.ZERO : 
                BigDecimal.valueOf(correctCount).divide(BigDecimal.valueOf(questions.size()), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        ProgressRecord progress = progressRecordRepository.findByLearnerProfileIdAndModuleIdAndTenantId(profile.getId(), quiz.getModule().getId(), tenantId)
                .orElse(new ProgressRecord());
        
        progress.setLearnerProfile(profile);
        progress.setModule(quiz.getModule());
        progress.setTenantId(tenantId);
        progress.setStatus("COMPLETED");
        progress.setScore(score);
        progressRecordRepository.save(progress);

        return score;
    }
}
