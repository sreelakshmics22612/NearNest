package com.nearnest.config;

import com.nearnest.model.Category;
import com.nearnest.repository.CategoryRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeCategories(
            CategoryRepository categoryRepository) {

        return args -> {

            addCategory(
                    categoryRepository,
                    "Food & Home Kitchen",
                    "🍛"
            );

            addCategory(
                    categoryRepository,
                    "Handmade & Crafts",
                    "🎨"
            );

            addCategory(
                    categoryRepository,
                    "Fashion & Clothing",
                    "👗"
            );

            addCategory(
                    categoryRepository,
                    "Beauty & Personal Care",
                    "✂️"
            );

            addCategory(
                    categoryRepository,
                    "Home Services",
                    "🏠"
            );

            addCategory(
                    categoryRepository,
                    "Repair & Maintenance",
                    "🔧"
            );

            addCategory(
                    categoryRepository,
                    "Tutoring & Education",
                    "📚"
            );

            addCategory(
                    categoryRepository,
                    "Health & Wellness",
                    "💚"
            );

            addCategory(
                    categoryRepository,
                    "Local Retail",
                    "🛍️"
            );

            addCategory(
                    categoryRepository,
                    "Other",
                    "✨"
            );

            System.out.println(
                    "NearNest categories initialized successfully."
            );
        };
    }


    private void addCategory(
            CategoryRepository repository,
            String name,
            String icon) {

        if (repository.findByName(name).isEmpty()) {

            Category category =
                    new Category(name, icon);

            repository.save(category);

            System.out.println(
                    "Added category: " + name
            );
        }
    }
}