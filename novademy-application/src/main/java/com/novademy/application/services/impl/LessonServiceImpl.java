package com.novademy.application.services.impl;

import com.novademy.application.entities.Lesson;
import com.novademy.application.external.cloudinary.CloudinaryService;
import com.novademy.application.mapping.ContractMapping;
import com.novademy.application.repositories.LessonRepository;
import com.novademy.application.services.LessonService;
import com.novademy.contracts.requests.lesson.CreateLessonRequest;
import com.novademy.contracts.requests.lesson.UpdateLessonRequest;
import com.novademy.contracts.responses.lesson.LessonResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<LessonResponse> getByCourseId(UUID courseId) {
        return lessonRepository.findByCourseId(courseId)
            .stream()
            .map(ContractMapping::toLessonResponse)
            .collect(Collectors.toList());
    }

    @Override
    public LessonResponse getById(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Lesson ID."));
        return ContractMapping.toLessonResponse(lesson);
    }

    @Override
    public LessonResponse create(CreateLessonRequest request) {
        Lesson lesson = ContractMapping.toLesson(request);
        MultipartFile video = request.video();
        if (video != null) {
            try {
                var result = cloudinaryService.uploadVideo(video, "lessons");
                lesson.setVideoUrl((String) result.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to upload lesson video.", ex);
            }
        }
        MultipartFile image = request.image();
        if (image != null) {
            try {
                var res = cloudinaryService.uploadImage(image, "lessons");
                lesson.setImageUrl((String) res.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to upload lesson image.", ex);
            }
        }
        lessonRepository.save(lesson);
        return ContractMapping.toLessonResponse(lesson);
    }

    @Override
    public LessonResponse update(UUID id, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Lesson ID."));

        lesson.setTitle(request.title());
        lesson.setDescription(request.description());
        MultipartFile video = request.video();
        if (video != null) {
            try {
                if (lesson.getVideoUrl() != null) {
                    cloudinaryService.deleteFile(lesson.getVideoUrl(), "video");
                }
                var result = cloudinaryService.uploadVideo(video, "lessons");
                lesson.setVideoUrl((String) result.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to update lesson video.", ex);
            }
        }
        lesson.setOrder(request.order());
        lesson.setTranscript(request.transcript());
        MultipartFile image = request.image();
        if (image != null) {
            try {
                if (lesson.getImageUrl() != null) {
                    cloudinaryService.deleteFile(lesson.getImageUrl(), "image");
                }
                var res = cloudinaryService.uploadImage(image, "lessons");
                lesson.setImageUrl((String) res.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to update lesson image.", ex);
            }
        }
        lesson.setUpdatedAt(java.time.LocalDateTime.now());
        lessonRepository.save(lesson);
        return ContractMapping.toLessonResponse(lesson);
    }

    @Override
    public void delete(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Lesson ID."));
        if (lesson.getVideoUrl() != null) {
            try {
                cloudinaryService.deleteFile(lesson.getVideoUrl(), "video");
            } catch (Exception ex) {
                throw new RuntimeException("Failed to delete lesson video.", ex);
            }
        }
        if (lesson.getImageUrl() != null) {
            try {
                cloudinaryService.deleteFile(lesson.getImageUrl(), "image");
            } catch (Exception ex) {
                throw new RuntimeException("Failed to delete lesson image.", ex);
            }
        }
        lessonRepository.delete(lesson);
    }
} 