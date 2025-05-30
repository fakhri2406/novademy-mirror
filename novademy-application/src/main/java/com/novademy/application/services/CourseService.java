package com.novademy.application.services;

import com.novademy.contracts.requests.course.CreateCourseRequest;
import com.novademy.contracts.requests.course.UpdateCourseRequest;
import com.novademy.contracts.responses.course.CourseResponse;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<CourseResponse> getAll();
    CourseResponse getById(UUID id);
    CourseResponse create(CreateCourseRequest request);
    CourseResponse update(UUID id, UpdateCourseRequest request);
    void delete(UUID id);
} 