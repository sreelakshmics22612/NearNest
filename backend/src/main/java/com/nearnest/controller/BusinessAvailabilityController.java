package com.nearnest.controller;

import com.nearnest.model.BusinessAvailability;
import com.nearnest.service.BusinessAvailabilityService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/businesses")
public class BusinessAvailabilityController {

    private final BusinessAvailabilityService availabilityService;

    public BusinessAvailabilityController(
            BusinessAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // Get unavailable dates
    @GetMapping("/{businessId}/availability")
    public ResponseEntity<List<Map<String, Object>>> getAvailability(
            @PathVariable Long businessId) {

        List<BusinessAvailability> records =
                availabilityService.getBusinessAvailability(businessId);

        List<Map<String, Object>> response =
                records.stream()
                        .map(record -> {

                            Map<String, Object> data =
                                    new HashMap<>();

                            data.put(
                                    "availabilityId",
                                    record.getAvailabilityId()
                            );

                            data.put(
                                    "unavailableDate",
                                    record.getUnavailableDate()
                            );

                            data.put(
                                    "reason",
                                    record.getReason()
                            );

                            data.put(
                                    "createdAt",
                                    record.getCreatedAt()
                            );

                            return data;
                        })
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Mark date unavailable
    @PostMapping("/{businessId}/availability")
    public ResponseEntity<Map<String, Object>> saveAvailability(
            @PathVariable Long businessId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) String reason) {

        BusinessAvailability record =
                availabilityService.saveAvailability(
                        businessId,
                        date,
                        reason
                );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "availabilityId",
                record.getAvailabilityId()
        );

        response.put(
                "unavailableDate",
                record.getUnavailableDate()
        );

        response.put(
                "reason",
                record.getReason()
        );

        response.put(
                "createdAt",
                record.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    // Check one date
    @GetMapping("/{businessId}/availability/{date}")
    public ResponseEntity<Map<String, Object>>
    getAvailabilityForDate(
            @PathVariable Long businessId,
            @PathVariable LocalDate date) {

        BusinessAvailability record =
                availabilityService.getAvailabilityForDate(
                        businessId,
                        date
                );

        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "availabilityId",
                record.getAvailabilityId()
        );

        response.put(
                "unavailableDate",
                record.getUnavailableDate()
        );

        response.put(
                "reason",
                record.getReason()
        );

        response.put(
                "createdAt",
                record.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    // Make date available again
    @DeleteMapping("/{businessId}/availability/{date}")
    public ResponseEntity<Void> deleteAvailability(
            @PathVariable Long businessId,
            @PathVariable LocalDate date) {

        availabilityService.deleteAvailability(
                businessId,
                date
        );

        return ResponseEntity.noContent().build();
    }
}