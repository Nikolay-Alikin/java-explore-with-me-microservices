package ru.yandex.practicum.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.location.model.dto.CreateLocationDto;
import ru.yandex.practicum.location.model.dto.LocationDto;
import ru.yandex.practicum.location.model.dto.UpdateLocationDto;

@FeignClient("location")
public interface LocationClient {

    @PostMapping("/admin/locations")
    LocationDto create(@RequestBody @Valid CreateLocationDto createLocationDto);

    @GetMapping("/admin/locations/{locId}")
    LocationDto getById(@PathVariable @Positive long locId);

    @PatchMapping("/admin/locations/{locId}")
    LocationDto update(@RequestBody UpdateLocationDto updateLocationDto,
            @PathVariable @Positive long locId);

    @DeleteMapping("/admin/locations/{locId}")
    void deleteById(@PathVariable long locId);

    @GetMapping("/admin/locations/lat/{lat}/lon/{lon}")
    LocationDto getByCoordinates(@PathVariable @Positive double lat,
            @PathVariable double lon);
}
