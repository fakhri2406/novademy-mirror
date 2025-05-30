package com.novademy.application.mapping;

import com.novademy.application.entities.Course;
import com.novademy.application.entities.Lesson;
import com.novademy.application.entities.Subscription;
import com.novademy.application.entities.User;
import com.novademy.contracts.requests.auth.RegisterRequest;
import com.novademy.contracts.requests.course.CreateCourseRequest;
import com.novademy.contracts.requests.lesson.CreateLessonRequest;
import com.novademy.contracts.requests.packages.CreatePackageRequest;
import com.novademy.contracts.requests.subscription.SubscriptionRequest;
import com.novademy.contracts.responses.auth.AuthResponse;
import com.novademy.contracts.responses.course.CourseResponse;
import com.novademy.contracts.responses.lesson.LessonResponse;
import com.novademy.contracts.responses.packages.PackageResponse;
import com.novademy.contracts.responses.subscription.SubscriptionResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.stream.Collectors;

public class ContractMapping {
    // Auth
    public static User toUser(RegisterRequest req) {
        return User.builder()
            .id(UUID.randomUUID())
            .username(req.username())
            .password(req.password())
            .salt(UUID.randomUUID().toString())
            .firstName(req.firstName())
            .lastName(req.lastName())
            .email(req.email())
            .phoneNumber(req.phoneNumber())
            .roleId(req.roleId())
            .group(req.group())
            .sector(req.sector())
            .profilePictureUrl(null)
            .registeredAt(LocalDateTime.now())
            .lastLoginAt(LocalDateTime.now())
            .emailVerificationCode(null)
            .emailVerificationExpiry(null)
            .isEmailVerified(false)
            .build();
    }

    public static AuthResponse toAuthResponse(String accessToken, String refreshToken) {
        return new AuthResponse(accessToken, refreshToken);
    }

    // Course
    public static Course toCourse(CreateCourseRequest req) {
        return Course.builder()
            .id(UUID.randomUUID())
            .title(req.title())
            .description(req.description())
            .subject(req.subject())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static CourseResponse toCourseResponse(Course c) {
        return new CourseResponse(
            c.getId(),
            c.getTitle(),
            c.getDescription(),
            c.getSubject(),
            c.getImageUrl(),
            c.getCreatedAt().atZone(ZoneOffset.UTC).toInstant(),
            c.getUpdatedAt().atZone(ZoneOffset.UTC).toInstant()
        );
    }

    // Lesson
    public static Lesson toLesson(CreateLessonRequest req) {
        return Lesson.builder()
            .id(UUID.randomUUID())
            .title(req.title())
            .description(req.description())
            .videoUrl(null)
            .order(req.order())
            .isFree(req.isFree())
            .transcript(req.transcript())
            .courseId(req.courseId())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static LessonResponse toLessonResponse(Lesson l) {
        return new LessonResponse(
            l.getId(),
            l.getTitle(),
            l.getDescription(),
            l.getVideoUrl(),
            l.getOrder(),
            l.getIsFree(),
            l.getTranscript(),
            l.getImageUrl(),
            l.getCreatedAt().atZone(ZoneOffset.UTC).toInstant(),
            l.getUpdatedAt().atZone(ZoneOffset.UTC).toInstant(),
            l.getCourseId()
        );
    }

    // Package
    public static com.novademy.application.entities.Package toPackage(CreatePackageRequest req) {
        return com.novademy.application.entities.Package.builder()
            .id(UUID.randomUUID())
            .title(req.title())
            .description(req.description())
            .price(req.price())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static PackageResponse toPackageResponse(com.novademy.application.entities.Package p) {
        return new PackageResponse(
            p.getId(),
            p.getTitle(),
            p.getDescription(),
            p.getPrice(),
            p.getImageUrl(),
            p.getCreatedAt().atZone(ZoneOffset.UTC).toInstant(),
            p.getUpdatedAt().atZone(ZoneOffset.UTC).toInstant(),
            p.getCourses().stream().map(Course::getId).collect(Collectors.toList())
        );
    }

    // Subscription
    public static Subscription toSubscription(SubscriptionRequest req) {
        return Subscription.builder()
            .id(UUID.randomUUID())
            .userId(req.userId())
            .packageId(req.packageId())
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusYears(1))
            .build();
    }

    public static SubscriptionResponse toSubscriptionResponse(Subscription s) {
        return new SubscriptionResponse(
            s.getId(),
            s.getUserId(),
            s.getPackageId(),
            s.getStartDate().atZone(ZoneOffset.UTC).toInstant(),
            s.getEndDate().atZone(ZoneOffset.UTC).toInstant(),
            s.isActive()
        );
    }
} 