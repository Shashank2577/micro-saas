package com.microsaas.educationos.controller;

import com.microsaas.educationos.dto.CourseDto;
import com.microsaas.educationos.dto.ModuleDto;
import com.microsaas.educationos.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.createCourse(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable UUID id, @RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<ModuleDto>> getModules(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getModules(courseId));
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<ModuleDto> createModule(@PathVariable UUID courseId, @RequestBody ModuleDto dto) {
        return ResponseEntity.ok(courseService.createModule(courseId, dto));
    }
}
