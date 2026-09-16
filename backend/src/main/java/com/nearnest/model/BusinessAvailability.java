package com.nearnest.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "business_availability",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"business_id", "unavailable_date"}
        )
    }
)
public class BusinessAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Long availabilityId;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessDetails business;

    @Column(name = "unavailable_date", nullable = false)
    private LocalDate unavailableDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BusinessAvailability() {
    }

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public BusinessDetails getBusiness() {
        return business;
    }

    public void setBusiness(BusinessDetails business) {
        this.business = business;
    }

    public LocalDate getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(LocalDate unavailableDate) {
        this.unavailableDate = unavailableDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}