package com.nearnest.service;

import com.nearnest.model.BusinessAvailability;
import com.nearnest.model.BusinessDetails;
import com.nearnest.repository.BusinessAvailabilityRepository;
import com.nearnest.repository.BusinessDetailsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessAvailabilityService {

    private final BusinessAvailabilityRepository availabilityRepository;
    private final BusinessDetailsRepository businessRepository;

    public BusinessAvailabilityService(
            BusinessAvailabilityRepository availabilityRepository,
            BusinessDetailsRepository businessRepository) {

        this.availabilityRepository = availabilityRepository;
        this.businessRepository = businessRepository;
    }

    // Get all unavailable dates for a business
    public List<BusinessAvailability> getBusinessAvailability(
            Long businessId) {

        businessRepository.findById(businessId)
                .orElseThrow(() ->
                        new RuntimeException("Business not found.")
                );

        return availabilityRepository
                .findByBusiness_BusinessIdOrderByUnavailableDateAsc(
                        businessId
                );
    }

    // Mark a date as unavailable
    @Transactional
public BusinessAvailability saveAvailability(
        Long businessId,
        LocalDate date,
        String reason) {

    businessRepository.findById(businessId)
            .orElseThrow(() ->
                    new RuntimeException("Business not found.")
            );

    BusinessAvailability existing =
            availabilityRepository
                    .findByBusiness_BusinessIdAndUnavailableDate(
                            businessId,
                            date
                    )
                    .orElse(null);

    if (existing != null) {

        existing.setReason(reason);

        return availabilityRepository.save(existing);
    }

    availabilityRepository.insertAvailability(
            businessId,
            date,
            reason
    );

    return availabilityRepository
            .findByBusiness_BusinessIdAndUnavailableDate(
                    businessId,
                    date
            )
            .orElseThrow(() ->
                    new RuntimeException(
                            "Availability was not saved."
                    )
            );
}
    // Check one particular date
    public BusinessAvailability getAvailabilityForDate(
            Long businessId,
            LocalDate date) {

        businessRepository.findById(businessId)
                .orElseThrow(() ->
                        new RuntimeException("Business not found.")
                );

        return availabilityRepository
                .findByBusiness_BusinessIdAndUnavailableDate(
                        businessId,
                        date
                )
                .orElse(null);
    }

    // Make a previously unavailable date available again
    public void deleteAvailability(
            Long businessId,
            LocalDate date) {

        BusinessAvailability availability =
                availabilityRepository
                        .findByBusiness_BusinessIdAndUnavailableDate(
                                businessId,
                                date
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Unavailable date not found."
                                )
                        );

        availabilityRepository.delete(availability);
    }
}