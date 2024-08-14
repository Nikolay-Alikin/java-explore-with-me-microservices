package com.github.artemlv.ewm.category.controller;

import com.github.artemlv.ewm.category.model.Category;
import com.github.artemlv.ewm.category.model.dto.CategoryDto;
import com.github.artemlv.ewm.category.model.dto.CreateCategoryDto;
import com.github.artemlv.ewm.category.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
    private static final String SIMPLE_NAME = Category.class.getSimpleName();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        log.debug("Request to create a {} - {}", SIMPLE_NAME, createCategoryDto);

        return categoryService.create(createCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable @Positive long catId,
                              @Valid @RequestBody CreateCategoryDto createCategoryDto) {
        log.debug("Request to update {} - {} by id - {}", SIMPLE_NAME, createCategoryDto, catId);

        return categoryService.update(createCategoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long catId) {
        log.debug("Request to delete a {} by id - {}", SIMPLE_NAME, catId);
        categoryService.deleteById(catId);
    }
}
