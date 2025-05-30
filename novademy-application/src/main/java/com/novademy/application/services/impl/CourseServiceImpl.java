package com.novademy.application.services.impl;

import com.novademy.application.entities.Course;
import com.novademy.application.external.cloudinary.CloudinaryService;
import com.novademy.application.mapping.ContractMapping;
import com.novademy.application.repositories.CourseRepository;
import com.novademy.application.services.CourseService;
import com.novademy.contracts.requests.course.CreateCourseRequest;
import com.novademy.contracts.requests.course.UpdateCourseRequest;
import com.novademy.contracts.responses.course.CourseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
            .stream()
            .map(ContractMapping::toCourseResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CourseResponse getById(UUID id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Course ID."));
        return ContractMapping.toCourseResponse(course);
    }

    @Override
    public CourseResponse create(CreateCourseRequest request) {
        Course course = ContractMapping.toCourse(request);
        MultipartFile image = request.image();
        if (image != null) {
            try {
                var result = cloudinaryService.uploadImage(image, "courses");
                course.setImageUrl((String) result.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to upload course image.", ex);
            }
        }
        course.setCreatedAt(course.getCreatedAt());
        course.setUpdatedAt(course.getUpdatedAt());
        courseRepository.save(course);
        return ContractMapping.toCourseResponse(course);
    }

    @Override
    public CourseResponse update(UUID id, UpdateCourseRequest request) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Course ID."));

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setSubject(request.subject());
        MultipartFile image = request.image();
        if (image != null) {
            try {
                if (course.getImageUrl() != null) {
                    cloudinaryService.deleteFile(course.getImageUrl(), "image");
                }
                var result = cloudinaryService.uploadImage(image, "courses");
                course.setImageUrl((String) result.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to update course image.", ex);
            }
        }
        course.setUpdatedAt(java.time.LocalDateTime.now());
        courseRepository.save(course);
        return ContractMapping.toCourseResponse(course);
    }

    @Override
    public void delete(UUID id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Course ID."));
        if (course.getImageUrl() != null) {
            try {
                cloudinaryService.deleteFile(course.getImageUrl(), "image");
            } catch (Exception ex) {
                throw new RuntimeException("Failed to delete course image.", ex);
            }
        }
        courseRepository.delete(course);
    }
} 