package ru.yandex.practicum.event.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.dto.EventMinDto;

@Component
@RequiredArgsConstructor
public class EventToEventDtoConverter implements Converter<Event, EventMinDto> {

    @Override
    public EventMinDto convert(final Event source) {
        return EventMinDto.builder()
                .id(source.getId())
                .annotation(source.getAnnotation())
                .category(source.getCategory())
                .confirmedRequests(source.getConfirmedRequests())
                .createdOn(source.getCreatedOn())
                .description(source.getDescription())
                .eventDate(source.getEventDate())
                .initiator(source.getInitiator())
                .location(source.getLocation())
                .paid(source.isPaid())
                .participantLimit(source.getParticipantLimit())
                .publishedOn(source.getPublishedOn())
                .requestModeration(source.isRequestModeration())
                .state(source.getState())
                .title(source.getTitle())
                .views(source.getViews())
                .build();
    }
}
