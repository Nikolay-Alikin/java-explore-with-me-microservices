package ru.yandex.practicum.category.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.model.dto.CategoryDto;

@Component
public class CategoryToCategoryDto implements Converter<Category, CategoryDto> {

    @Override
    public CategoryDto convert(final Category source) {
        return CategoryDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
