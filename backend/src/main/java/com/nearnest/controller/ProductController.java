package com.nearnest.controller;

import com.nearnest.model.Product;
import com.nearnest.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    // =========================================================
    // ADD PRODUCT
    // =========================================================

    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody Product product) {

        Product savedProduct =
                productService.addProduct(product);

        return new ResponseEntity<>(
                savedProduct,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET PRODUCTS FOR BUSINESS
    // =========================================================

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<Product>> getProductsByBusinessId(
            @PathVariable Long businessId) {

        return ResponseEntity.ok(
                productService.getProductsByBusinessId(businessId)
        );
    }


    // =========================================================
    // GET ONE PRODUCT
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }


    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.updateProduct(id, product)
        );
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // UPDATE AVAILABILITY
    // =========================================================

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Product> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {

        return ResponseEntity.ok(
                productService.updateAvailability(
                        id,
                        available
                )
        );
    }


    // =========================================================
    // UPLOAD PRODUCT IMAGE
    // =========================================================

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadProductImage(
            @RequestParam("file") MultipartFile file) {

        try {

            // -------------------------------------------------
            // VALIDATE FILE
            // -------------------------------------------------

            if (file == null || file.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Please select an image.");
            }


            // -------------------------------------------------
            // VALIDATE CONTENT TYPE
            // -------------------------------------------------

            String contentType =
                    file.getContentType();

            if (contentType == null ||
                    !contentType.startsWith("image/")) {

                return ResponseEntity
                        .badRequest()
                        .body("Only image files are allowed.");
            }


            // -------------------------------------------------
            // CREATE UPLOAD DIRECTORY
            // -------------------------------------------------

            Path uploadDirectory =
                    Paths.get(
                            "uploads",
                            "products"
                    );

            Files.createDirectories(
                    uploadDirectory
            );


            // -------------------------------------------------
            // CREATE SAFE FILE NAME
            // -------------------------------------------------

            String originalFilename =
                    file.getOriginalFilename();

            if (originalFilename == null ||
                    originalFilename.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Invalid image filename.");
            }

            String safeFilename =
                    originalFilename
                            .replaceAll(
                                    "[^a-zA-Z0-9._-]",
                                    "_"
                            );

            String filename =
                    System.currentTimeMillis()
                            + "_"
                            + safeFilename;


            // -------------------------------------------------
            // SAVE FILE
            // -------------------------------------------------

            Path filePath =
                    uploadDirectory.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            // -------------------------------------------------
            // RETURN PUBLIC IMAGE URL
            // -------------------------------------------------

            String imageUrl =
                    "http://localhost:8080/uploads/products/"
                            + filename;

            System.out.println(
                    "PRODUCT IMAGE SAVED: "
                            + filePath.toAbsolutePath()
            );

            System.out.println(
                    "PRODUCT IMAGE URL: "
                            + imageUrl
            );

            return ResponseEntity.ok(
                    imageUrl
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            "Failed to upload product image: "
                                    + e.getMessage()
                    );
        }
    }
}