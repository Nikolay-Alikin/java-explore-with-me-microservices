package com.github.artemlv.ewm.category.controller;

import com.github.artemlv.ewm.category.model.Category;
import com.github.artemlv.ewm.category.model.dto.CategoryDto;
import com.github.artemlv.ewm.category.service.CategoryService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;
    private static final String SIMPLE_NAME = Category.class.getSimpleName();

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                           @RequestParam(defaultValue = "10") @Positive final int size) {
        log.debug("Request for all {} beginning - {} size - {}", SIMPLE_NAME, from, size);

        return categoryService.getAll(from, size);
    }

    @GetMapping
    @RequestMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Positive long catId) {
        log.debug("Request to get a {} by id - {}", SIMPLE_NAME, catId);

        return categoryService.getById(catId);
    }
}
