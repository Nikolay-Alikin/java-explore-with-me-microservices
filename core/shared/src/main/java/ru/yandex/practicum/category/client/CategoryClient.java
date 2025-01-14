package ru.yandex.practicum.category.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.category.model.dto.CategoryDto;
import ru.yandex.practicum.category.model.dto.CreateCategoryDto;

@FeignClient("category")
public interface CategoryClient {

    @PostMapping("/admin/categories")
    CategoryDto create(@RequestBody @Valid CreateCategoryDto createCategoryDto);

    @PatchMapping("/admin/categories/{catId}")
    CategoryDto update(@PathVariable @Positive long catId,
            @RequestBody @Valid CreateCategoryDto createCategoryDto);

    @DeleteMapping("/admin/categories/{catId}")
    void delete(@PathVariable @Positive long catId);

    @GetMapping("/categories")
    List<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size);

    @GetMapping("/categories/{catId}")
    CategoryDto getById(@PathVariable @Positive long catId);
}
