package com.microsaas.educationos.service;

import com.microsaas.educationos.domain.entity.Course;
import com.microsaas.educationos.domain.repository.CourseRepository;
import com.microsaas.educationos.dto.CourseDto;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetCourse() {
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        course.setTenantId(tenantId);
        course.setTitle("Test Course");

        when(courseRepository.findByIdAndTenantId(courseId, tenantId)).thenReturn(Optional.of(course));

        CourseDto result = courseService.getCourse(courseId);

        assertNotNull(result);
        assertEquals("Test Course", result.getTitle());
    }

    @Test
    void testCreateCourse() {
        CourseDto inputDto = new CourseDto();
        inputDto.setTitle("New Course");
        inputDto.setDescription("Desc");

        Course savedCourse = new Course();
        savedCourse.setId(UUID.randomUUID());
        savedCourse.setTenantId(tenantId);
        savedCourse.setTitle("New Course");

        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseDto result = courseService.createCourse(inputDto);

        assertNotNull(result);
        assertEquals("New Course", result.getTitle());
    }
}
