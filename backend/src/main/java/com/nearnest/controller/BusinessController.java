package com.nearnest.controller;

import com.nearnest.dto.BusinessRequest;
import com.nearnest.model.BusinessDetails;
import com.nearnest.service.BusinessService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createBusiness(
            @ModelAttribute BusinessRequest request) {

        try {

            BusinessDetails business =
                    businessService.createBusiness(request);

            return ResponseEntity.ok(business);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to save uploaded files.");
        }
    }

    @GetMapping
    public ResponseEntity<List<BusinessDetails>> getAllBusinesses() {

        return ResponseEntity.ok(
                businessService.getAllBusinesses()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBusinessByUser(
            @PathVariable Long userId) {

        try {

            return ResponseEntity.ok(
                    businessService.getBusinessByUserId(userId)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BusinessDetails>>
    getBusinessesByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                businessService.getBusinessesByCategory(categoryId)
        );
    }
}