package ru.yandex.practicum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.model.dto.CompilationDto;
import ru.yandex.practicum.compilation.model.dto.CreateCompilationDto;
import ru.yandex.practicum.compilation.model.dto.UpdateCompilationDto;
import ru.yandex.practicum.event.client.EventClient;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.exception.type.NotFoundException;
import ru.yandex.practicum.storage.CompilationStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final EventClient client;
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final CompilationStorage compilationStorage;

    @Override
    public CompilationDto create(final CreateCompilationDto createCompilationDto) {
        Compilation compilation = cs.convert(createCompilationDto, Compilation.class);

        if (Objects.isNull(compilation)) {
            throw new NotFoundException("the number of events found does not correspond to the requirements");
        }
        var eventsIds = new ArrayList<>(createCompilationDto.events());
        if (!ObjectUtils.isEmpty(createCompilationDto.events())) {
            var events = client.getAll(null, null, null, eventsIds, null, null,
                    0, 10).stream().toList();

            if (events.size() != createCompilationDto.events().size()) {
                throw new NotFoundException("the number of events found does not correspond to the requirements");
            }

            compilation.setEvents(events.stream().map(EventFullDto::id).toList());
        }
        return cs.convert(compilationStorage.save(compilation), CompilationDto.class);
    }

    @Override
    public CompilationDto update(final UpdateCompilationDto updateCompilationDto, final long compId) {
        Compilation compilationInStorage = compilationStorage.getByIdOrElseThrow(compId);

        if (ObjectUtils.isEmpty(updateCompilationDto.pinned())) {
            compilationInStorage.setPinned(compilationInStorage.isPinned());
        }

        if (ObjectUtils.isEmpty(updateCompilationDto.title())) {
            compilationInStorage.setTitle(compilationInStorage.getTitle());
        }

        if (!ObjectUtils.isEmpty(updateCompilationDto.events())) {
            var eventsIds = new ArrayList<>(updateCompilationDto.events());
            var events = client.getAll(null, null, null, eventsIds, null, null,
                    0, Integer.MAX_VALUE).stream().toList();
            compilationInStorage.setEvents(events.stream().map(EventFullDto::id).toList());
        }
        log.info("Update compilation - {}", compilationInStorage);

        return cs.convert(compilationStorage.save(compilationInStorage), CompilationDto.class);
    }

    @Override
    public void delete(final long compId) {
        compilationStorage.existsByIdOrElseThrow(compId);
        compilationStorage.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getAll(final Boolean pinned, final int from, final int size) {
        PageRequest page = PageRequest.of(from / size, size);

        List<Compilation> compilations = ObjectUtils.isEmpty(pinned) ? compilationStorage.findAll(page)
                : compilationStorage.findAllByPinnedIs(pinned, page);

        return compilations.stream()
                .map(compilation -> cs.convert(compilation, CompilationDto.class))
                .toList();
    }

    @Override
    public CompilationDto getById(final long compId) {
        return cs.convert(compilationStorage.getByIdOrElseThrow(compId), CompilationDto.class);
    }
}
