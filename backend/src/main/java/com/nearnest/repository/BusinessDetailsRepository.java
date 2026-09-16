package com.nearnest.repository;

import com.nearnest.model.BusinessDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessDetailsRepository
        extends JpaRepository<BusinessDetails, Long> {

    Optional<BusinessDetails> findByUser_Id(Long userId);

    List<BusinessDetails> findByIsActiveTrue();

    List<BusinessDetails> findByCategory_CategoryId(Long categoryId);
}