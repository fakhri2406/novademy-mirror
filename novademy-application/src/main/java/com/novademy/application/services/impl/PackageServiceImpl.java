package com.novademy.application.services.impl;

import com.novademy.application.entities.Course;
import com.novademy.application.entities.Package;
import com.novademy.application.external.cloudinary.CloudinaryService;
import com.novademy.application.mapping.ContractMapping;
import com.novademy.application.repositories.CourseRepository;
import com.novademy.application.repositories.PackageRepository;
import com.novademy.application.services.PackageService;
import com.novademy.contracts.requests.packages.CreatePackageRequest;
import com.novademy.contracts.requests.packages.UpdatePackageRequest;
import com.novademy.contracts.responses.packages.PackageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;
    private final CourseRepository courseRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<PackageResponse> getAll() {
        return packageRepository.findAll()
            .stream()
            .map(ContractMapping::toPackageResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PackageResponse getById(UUID id) {
        Package pack = packageRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Package ID."));
        return ContractMapping.toPackageResponse(pack);
    }

    @Override
    public PackageResponse create(CreatePackageRequest request) {
        Package pack = ContractMapping.toPackage(request);
        // handle course associations
        Set<Course> courses = request.courseIds().stream()
            .map(courseId -> courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Invalid Course ID.")))
            .collect(Collectors.toSet());
        pack.setCourses(courses);
        MultipartFile image = request.image();
        if (image != null) {
            try {
                var res = cloudinaryService.uploadImage(image, "packages");
                pack.setImageUrl((String) res.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to upload package image.", ex);
            }
        }
        packageRepository.save(pack);
        return ContractMapping.toPackageResponse(pack);
    }

    @Override
    public PackageResponse update(UUID id, UpdatePackageRequest request) {
        Package pack = packageRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Package ID."));
        pack.setTitle(request.title());
        pack.setDescription(request.description());
        pack.setPrice(request.price());
        Set<Course> courses = request.courseIds().stream()
            .map(courseId -> courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Invalid Course ID.")))
            .collect(Collectors.toSet());
        pack.setCourses(courses);
        MultipartFile image = request.image();
        if (image != null) {
            try {
                if (pack.getImageUrl() != null) {
                    cloudinaryService.deleteFile(pack.getImageUrl(), "image");
                }
                var res = cloudinaryService.uploadImage(image, "packages");
                pack.setImageUrl((String) res.get("secure_url"));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to update package image.", ex);
            }
        }
        pack.setUpdatedAt(java.time.LocalDateTime.now());
        packageRepository.save(pack);
        return ContractMapping.toPackageResponse(pack);
    }

    @Override
    public void delete(UUID id) {
        Package pack = packageRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Invalid Package ID."));
        if (pack.getImageUrl() != null) {
            try {
                cloudinaryService.deleteFile(pack.getImageUrl(), "image");
            } catch (Exception ex) {
                throw new RuntimeException("Failed to delete package image.", ex);
            }
        }
        packageRepository.delete(pack);
    }
} 