package ru.yandex.practicum.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.model.dto.CategoryDto;
import ru.yandex.practicum.category.model.dto.CreateCategoryDto;
import ru.yandex.practicum.category.storage.CategoryStorage;
import ru.yandex.practicum.event.client.EventClient;
import ru.yandex.practicum.exception.type.ConflictException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final EventClient eventClient;
    private final CategoryStorage categoryStorage;

    @Override
    public CategoryDto create(CreateCategoryDto createCategoryDto) {
        Category category = cs.convert(createCategoryDto, Category.class);
        return cs.convert(categoryStorage.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto update(final CreateCategoryDto createCategoryDto, final long id) {
        Category category = categoryStorage.getByIdOrElseThrow(id);
        category.setName(createCategoryDto.name());
        log.info("Update category - {}", category);

        return cs.convert(categoryStorage.save(category), CategoryDto.class);
    }

    @Override
    public void deleteById(final long id) {
        boolean isEmpty = eventClient.getAll(null, null, List.of(id), null, null,
                        null, 0, 1)
                .isEmpty();

        if (isEmpty) {
            throw new ConflictException("Category with id " + id + " exists in Event");
        }
        categoryStorage.existsByIdOrElseThrow(id);
        categoryStorage.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAll(final int from, final int size) {
        return categoryStorage.getAll(from, size).stream()
                .map(category -> cs.convert(category, CategoryDto.class))
                .toList();
    }

    @Override
    public CategoryDto getById(final long id) {
        return cs.convert(categoryStorage.getByIdOrElseThrow(id), CategoryDto.class);
    }
}
