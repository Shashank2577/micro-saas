package com.microsaas.educationos.service;

import com.microsaas.educationos.domain.entity.Course;
import com.microsaas.educationos.domain.entity.Module;
import com.microsaas.educationos.domain.repository.CourseRepository;
import com.microsaas.educationos.domain.repository.ModuleRepository;
import com.microsaas.educationos.dto.CourseDto;
import com.microsaas.educationos.dto.ModuleDto;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<CourseDto> getCourses() {
        UUID tenantId = TenantContext.require();
        return courseRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourse(UUID id) {
        UUID tenantId = TenantContext.require();
        Course course = courseRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToDto(course);
    }

    public CourseDto createCourse(CourseDto dto) {
        UUID tenantId = TenantContext.require();
        Course course = new Course();
        course.setTenantId(tenantId);
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course = courseRepository.save(course);
        return mapToDto(course);
    }

    public CourseDto updateCourse(UUID id, CourseDto dto) {
        UUID tenantId = TenantContext.require();
        Course course = courseRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course = courseRepository.save(course);
        return mapToDto(course);
    }

    public void deleteCourse(UUID id) {
        UUID tenantId = TenantContext.require();
        Course course = courseRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        courseRepository.delete(course);
    }

    public List<ModuleDto> getModules(UUID courseId) {
        UUID tenantId = TenantContext.require();
        return moduleRepository.findByCourseIdAndTenantId(courseId, tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ModuleDto createModule(UUID courseId, ModuleDto dto) {
        UUID tenantId = TenantContext.require();
        Course course = courseRepository.findByIdAndTenantId(courseId, tenantId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Module module = new Module();
        module.setTenantId(tenantId);
        module.setCourse(course);
        module.setTitle(dto.getTitle());
        module.setContent(dto.getContent());
        module.setDifficultyLevel(dto.getDifficultyLevel());
        module.setOrderIndex(dto.getOrderIndex());
        
        module = moduleRepository.save(module);
        return mapToDto(module);
    }

    public ModuleDto updateModule(UUID id, ModuleDto dto) {
        UUID tenantId = TenantContext.require();
        Module module = moduleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        
        module.setTitle(dto.getTitle());
        module.setContent(dto.getContent());
        module.setDifficultyLevel(dto.getDifficultyLevel());
        module.setOrderIndex(dto.getOrderIndex());
        
        module = moduleRepository.save(module);
        return mapToDto(module);
    }

    private CourseDto mapToDto(Course entity) {
        CourseDto dto = new CourseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    private ModuleDto mapToDto(Module entity) {
        ModuleDto dto = new ModuleDto();
        dto.setId(entity.getId());
        dto.setCourseId(entity.getCourse().getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDifficultyLevel(entity.getDifficultyLevel());
        dto.setOrderIndex(entity.getOrderIndex());
        return dto;
    }
}
