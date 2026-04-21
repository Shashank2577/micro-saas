package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID projectId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void createTask_SetsProjectTenantAndTimestamps() {
        Task task = new Task();
        task.setTitle("Test Task");

        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        Task created = taskService.createTask(projectId, task);

        assertEquals(projectId, created.getProjectId());
        assertEquals(tenantId, created.getTenantId());
        assertNotNull(created.getCreatedAt());
        verify(taskRepository).save(task);
    }
}
