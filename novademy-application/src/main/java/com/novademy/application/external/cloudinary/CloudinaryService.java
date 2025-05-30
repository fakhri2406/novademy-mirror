package com.novademy.application.external.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map uploadImage(MultipartFile file, String folder) throws Exception;
    Map uploadVideo(MultipartFile file, String folder) throws Exception;
    void deleteFile(String publicId, String resourceType) throws Exception;
} 