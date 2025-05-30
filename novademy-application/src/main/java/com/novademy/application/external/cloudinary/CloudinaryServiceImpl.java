package com.novademy.application.external.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map uploadImage(MultipartFile file, String folder) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", folder));
    }

    @Override
    public Map uploadVideo(MultipartFile file, String folder) throws IOException {
        return cloudinary.uploader().uploadLarge(file.getBytes(),
                ObjectUtils.asMap("resource_type", "video", "folder", folder));
    }

    @Override
    public void deleteFile(String publicId, String resourceType) throws Exception {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", resourceType));
    }
} 