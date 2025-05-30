package com.novademy.application.repositories;

import com.novademy.application.entities.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {
} 