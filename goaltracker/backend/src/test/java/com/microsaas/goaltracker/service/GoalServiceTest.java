package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.entity.Milestone;
import com.microsaas.goaltracker.repository.GoalRepository;
import com.microsaas.goaltracker.repository.MilestoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @InjectMocks
    private GoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGoal() {
        Goal goal = new Goal();
        goal.setTenantId("tenant1");
        goal.setTitle("Emergency Fund");
        goal.setTargetAmount(new BigDecimal("10000.00"));
        goal.setDeadline(LocalDateTime.now().plusMonths(12));

        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = goalService.createGoal(goal);

        assertEquals("tenant1", result.getTenantId());
        assertEquals(new BigDecimal("10000.00"), result.getTargetAmount());

        // Verify 4 milestones were created
        verify(milestoneRepository, times(4)).save(any(Milestone.class));
    }
}
