package com.novademy.application.services;

import com.novademy.contracts.requests.lesson.CreateLessonRequest;
import com.novademy.contracts.requests.lesson.UpdateLessonRequest;
import com.novademy.contracts.responses.lesson.LessonResponse;

import java.util.List;
import java.util.UUID;

public interface LessonService {
    List<LessonResponse> getByCourseId(UUID courseId);
    LessonResponse getById(UUID id);
    LessonResponse create(CreateLessonRequest request);
    LessonResponse update(UUID id, UpdateLessonRequest request);
    void delete(UUID id);
} 