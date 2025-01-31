package ru.yandex.practicum.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.model.dto.CreateLocationDto;
import ru.yandex.practicum.location.model.dto.LocationDto;
import ru.yandex.practicum.location.model.dto.UpdateLocationDto;
import ru.yandex.practicum.storage.LocationStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final LocationStorage locationStorage;

    @Override
    public LocationDto create(CreateLocationDto createLocationDto) {
        Location location = cs.convert(createLocationDto, Location.class);
        return cs.convert(locationStorage.save(location), LocationDto.class);
    }

    @Override
    public LocationDto getById(final long id) {
        return cs.convert(locationStorage.findByIdOrElseThrow(id), LocationDto.class);
    }

    @Override
    public LocationDto updateById(final long id, final UpdateLocationDto updateLocationDto) {
        Location location = locationStorage.findByIdOrElseThrow(id);

        if (updateLocationDto.lat() != 0) {
            location.setLat(updateLocationDto.lat());
        }

        if (updateLocationDto.lon() != 0) {
            location.setLon(updateLocationDto.lon());
        }

        if (!ObjectUtils.isEmpty(updateLocationDto.name())) {
            location.setName(updateLocationDto.name());
        }

        if (updateLocationDto.radius() > 0) {
            location.setRadius(updateLocationDto.radius());
        }

        return cs.convert(locationStorage.save(location), LocationDto.class);
    }

    @Override
    public void deleteById(final long id) {
        locationStorage.existsByIdOrElseThrow(id);
        locationStorage.deleteById(id);
    }

    @Override
    public List<LocationDto> getAll(final String text, final int from, final int size) {
        return locationStorage.findAllByNameContainingIgnoreCase(text, from, size).stream()
                .map(location -> cs.convert(location, LocationDto.class))
                .toList();
    }
}
