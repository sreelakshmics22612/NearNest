package com.nearnest.repository;

import com.nearnest.model.BusinessAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BusinessAvailabilityRepository
        extends JpaRepository<BusinessAvailability, Long> {

    List<BusinessAvailability>
    findByBusiness_BusinessIdOrderByUnavailableDateAsc(
            Long businessId
    );

    Optional<BusinessAvailability>
    findByBusiness_BusinessIdAndUnavailableDate(
            Long businessId,
            LocalDate unavailableDate
    );

    @Modifying
    @Query(value = """
        INSERT INTO business_availability
        (business_id, unavailable_date, reason, created_at)
        VALUES
        (:businessId, :date, :reason, CURRENT_TIMESTAMP)
        """, nativeQuery = true)
    int insertAvailability(
            @Param("businessId") Long businessId,
            @Param("date") LocalDate date,
            @Param("reason") String reason
    );

    @Modifying
    @Query(value = """
        UPDATE business_availability
        SET reason = :reason
        WHERE business_id = :businessId
        AND unavailable_date = :date
        """, nativeQuery = true)
    int updateAvailability(
            @Param("businessId") Long businessId,
            @Param("date") LocalDate date,
            @Param("reason") String reason
    );

    void deleteByBusiness_BusinessId(Long businessId);
}