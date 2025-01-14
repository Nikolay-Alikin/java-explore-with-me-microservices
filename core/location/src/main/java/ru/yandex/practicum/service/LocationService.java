package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.location.model.dto.CreateLocationDto;
import ru.yandex.practicum.location.model.dto.LocationDto;
import ru.yandex.practicum.location.model.dto.UpdateLocationDto;

public interface LocationService {

    LocationDto create(CreateLocationDto createLocationDto);

    LocationDto getById(final long id);

    LocationDto updateById(final long id, final UpdateLocationDto updateLocationDto);

    void deleteById(final long id);

    List<LocationDto> getAll(final String text, final int from, final int size);
}
