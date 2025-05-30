package com.novademy.application.services;

import com.novademy.contracts.requests.packages.CreatePackageRequest;
import com.novademy.contracts.requests.packages.UpdatePackageRequest;
import com.novademy.contracts.responses.packages.PackageResponse;

import java.util.List;
import java.util.UUID;

public interface PackageService {
    List<PackageResponse> getAll();
    PackageResponse getById(UUID id);
    PackageResponse create(CreatePackageRequest request);
    PackageResponse update(UUID id, UpdatePackageRequest request);
    void delete(UUID id);
} 