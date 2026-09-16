package com.nearnest.service;

import com.nearnest.dto.BusinessRequest;
import com.nearnest.model.BusinessDetails;
import com.nearnest.model.Category;
import com.nearnest.model.User;
import com.nearnest.model.VerificationStatus;
import com.nearnest.repository.BusinessDetailsRepository;
import com.nearnest.repository.CategoryRepository;
import com.nearnest.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessService {

    private final BusinessDetailsRepository businessRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final Path uploadPath =
            Paths.get("uploads/businesses");


    public BusinessService(
            BusinessDetailsRepository businessRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {

        this.businessRepository = businessRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }


    // =========================
    // CREATE BUSINESS
    // =========================

    public BusinessDetails createBusiness(
            BusinessRequest request) throws IOException {

        // Check whether this user already has a business
        if (businessRepository
                .findByUser_Id(request.getUserId())
                .isPresent()) {

            throw new RuntimeException(
                    "Business already exists for this user.");
        }


        // Find owner
        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));


        // Find category
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found."));


        // Create upload directory
        Files.createDirectories(uploadPath);


        // =========================
        // SAVE LOGO
        // =========================

        String logoPath = null;

        if (request.getLogo() != null &&
                !request.getLogo().isEmpty()) {

            logoPath = saveFile(
                    request.getLogo(),
                    "logos");
        }


        // =========================
        // SAVE IDENTITY DOCUMENT
        // =========================

        String identityPath = null;

        if (request.getIdentityDocument() != null &&
                !request.getIdentityDocument().isEmpty()) {

            identityPath = saveFile(
                    request.getIdentityDocument(),
                    "identity");
        }


        // =========================
        // CREATE BUSINESS
        // =========================

        BusinessDetails business =
                new BusinessDetails();

        business.setUser(user);

        business.setBusinessName(
                request.getBusinessName());

        business.setCategory(category);

        business.setContactNumber(
                request.getContactNumber());

        business.setLocation(
                request.getLocation());

        business.setLatitude(
                request.getLatitude());

        business.setLongitude(
                request.getLongitude());

        business.setLogoPath(logoPath);

        business.setIdentityDocumentPath(identityPath);

        business.setVerificationStatus(
                VerificationStatus.PENDING);

        business.setActive(true);


        return businessRepository.save(business);
    }


    // =========================
    // SAVE FILE
    // =========================

    private String saveFile(
            MultipartFile file,
            String folder) throws IOException {

        Path directory =
                uploadPath.resolve(folder);

        Files.createDirectories(directory);


        String originalName =
                file.getOriginalFilename();

        String extension = "";

        if (originalName != null &&
                originalName.contains(".")) {

            extension = originalName.substring(
                    originalName.lastIndexOf("."));
        }


        String fileName =
                UUID.randomUUID() + extension;

        Path filePath =
                directory.resolve(fileName);


        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);


        return filePath.toString();
    }


    // =========================
    // GET BUSINESS BY OWNER
    // =========================

    public BusinessDetails getBusinessByUserId(
            Long userId) {

        return businessRepository
                .findByUser_Id(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Business not found."));
    }


    // =========================
    // GET ALL ACTIVE BUSINESSES
    // =========================

    public List<BusinessDetails> getAllBusinesses() {

        return businessRepository
                .findByIsActiveTrue();
    }


    // =========================
    // GET BUSINESSES BY CATEGORY
    // =========================

    public List<BusinessDetails> getBusinessesByCategory(
            Long categoryId) {

        return businessRepository
                .findByCategory_CategoryId(categoryId);
    }
}