package com.microsaas.constructioniq.project;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SafetyIncidentRepository incidentRepository;
    private final AiService aiService;

    @Value("${cc.ai.default-model:gpt-3.5-turbo}")
    private String aiModel;

    public Project createProject(Project project) {
        project.setTenantId(TenantContext.require());
        project.setCreatedAt(OffsetDateTime.now());
        project.setUpdatedAt(OffsetDateTime.now());
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjects() {
        return projectRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Project getProject(UUID id) {
        return projectRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project updateProject(UUID id, Project projectUpdate) {
        Project project = getProject(id);
        project.setName(projectUpdate.getName());
        project.setDescription(projectUpdate.getDescription());
        project.setStatus(projectUpdate.getStatus());
        project.setBudget(projectUpdate.getBudget());
        project.setStartDate(projectUpdate.getStartDate());
        project.setEndDate(projectUpdate.getEndDate());
        project.setLocation(projectUpdate.getLocation());
        project.setUpdatedAt(OffsetDateTime.now());
        return projectRepository.save(project);
    }

    public void deleteProject(UUID id) {
        Project project = getProject(id);
        projectRepository.delete(project);
    }

    public String getRiskAssessment(UUID projectId) {
        Project project = getProject(projectId);
        List<Task> tasks = taskRepository.findByProjectIdAndTenantId(projectId, TenantContext.require());
        List<SafetyIncident> incidents = incidentRepository.findByProjectIdAndTenantId(projectId, TenantContext.require());

        String prompt = String.format(
            "Analyze the following construction project data and identify potential risks (financial, safety, schedule). Provide mitigation strategies.\n" +
            "Project: %s\n" +
            "Tasks: %s\n" +
            "Safety Incidents: %s",
            project.toString(),
            tasks.stream().map(Task::toString).collect(Collectors.joining(", ")),
            incidents.stream().map(SafetyIncident::toString).collect(Collectors.joining(", "))
        );

        ChatRequest request = new ChatRequest(
            aiModel,
            List.of(new ChatMessage("user", prompt)),
            0.7,
            1000
        );

        return aiService.chat(request).content();
    }

    public String optimizeSchedule(UUID projectId) {
        Project project = getProject(projectId);
        List<Task> tasks = taskRepository.findByProjectIdAndTenantId(projectId, TenantContext.require());

        String prompt = String.format(
            "Analyze the current task list for the construction project and suggest optimizations to the schedule to reduce duration and improve resource efficiency.\n" +
            "Project: %s\n" +
            "Tasks: %s",
            project.toString(),
            tasks.stream().map(Task::toString).collect(Collectors.joining(", "))
        );

        ChatRequest request = new ChatRequest(
            aiModel,
            List.of(new ChatMessage("user", prompt)),
            0.7,
            1000
        );

        return aiService.chat(request).content();
    }
}
