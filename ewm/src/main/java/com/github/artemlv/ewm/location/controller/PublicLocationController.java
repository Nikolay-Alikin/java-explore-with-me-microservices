package com.github.artemlv.ewm.location.controller;

import com.github.artemlv.ewm.location.model.dto.LocationDto;
import com.github.artemlv.ewm.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.Location;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class PublicLocationController {
    private final LocationService locationService;
    private static final String SIMPLE_NAME = Location.class.getSimpleName();

    @GetMapping
    public List<LocationDto> getLocations(@RequestParam(required = false) final String text,
                                          @RequestParam(defaultValue = "0") final int from,
                                          @RequestParam(defaultValue = "10") final int size) {
        log.debug("Public {} for all locations by text - {} beginning - {} size - {}", SIMPLE_NAME, text, from, size);
        return locationService.getAll(text, from, size);
    }
}
