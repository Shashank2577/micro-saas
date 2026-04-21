package com.microsaas.constructioniq.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Test Project");
    }

    @Test
    void createProject() throws Exception {
        when(projectService.createProject(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void getProjects() throws Exception {
        when(projectService.getProjects()).thenReturn(List.of(project));

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Project"));
    }

    @Test
    void getProjectById() throws Exception {
        when(projectService.getProjectById(project.getId())).thenReturn(project);

        mockMvc.perform(get("/api/v1/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void updateProject() throws Exception {
        when(projectService.updateProject(eq(project.getId()), any(Project.class))).thenReturn(project);

        mockMvc.perform(put("/api/v1/projects/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void deleteProject() throws Exception {
        mockMvc.perform(delete("/api/v1/projects/" + project.getId()))
                .andExpect(status().isNoContent());
    }
}
