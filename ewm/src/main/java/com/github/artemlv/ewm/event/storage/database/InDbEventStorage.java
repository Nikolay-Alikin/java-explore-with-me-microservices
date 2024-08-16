package com.github.artemlv.ewm.event.storage.database;

import com.github.artemlv.ewm.event.model.Event;
import com.github.artemlv.ewm.event.storage.EventStorage;
import com.github.artemlv.ewm.exception.type.NotFoundException;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbEventStorage implements EventStorage {
    private final EventRepository eventRepository;
    private static final String SIMPLE_NAME = Event.class.getSimpleName();

    @Override
    public List<Event> findAll(final BooleanExpression predicate, final PageRequest pageRequest) {
        final List<Event> events = eventRepository.findAll(predicate, pageRequest).stream().toList();
        log.debug("Getting all {} : {}", SIMPLE_NAME, events);
        return events;
    }

    @Override
    public Optional<Event> getById(final long id) {
        final Optional<Event> event = eventRepository.findById(id);
        log.debug("Get Optional<{}> by id - {}", SIMPLE_NAME, id);
        return event;
    }

    @Override
    public Event getByIdOrElseThrow(final long id) {
        return getById(id).orElseThrow(() -> new NotFoundException(SIMPLE_NAME, id));
    }

    @Override
    public List<Event> findAllByInitiatorId(final long userId, final PageRequest pageRequest) {
        final List<Event> events = eventRepository.findByInitiatorId(userId, pageRequest);
        log.debug("Getting all by initiator {} : {}", SIMPLE_NAME, events);
        return events;
    }

    @Override
    public List<Event> findAllEventsByLocation(final double lat, final double lon) {
        final List<Event> locations = eventRepository.findByLocationLatAndLocationLon(lat, lon);
        log.debug("Getting a list of events by location coordinates - {}, {}, {}", SIMPLE_NAME, lat, lon);
        return locations;
    }

    @Override
    public List<Event> findAllByLocationAndRadius(final double lat, final double lon, final double radius) {
        final List<Event> locations = eventRepository.findByLocationLatAndLocationLonAndLocationRadius(lat, lon, radius);
        log.debug("Getting a list of events by location coordinates - {}, {}, {}, {}", SIMPLE_NAME, lat, lon, radius);
        return locations;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        final Event eventInStorage = eventRepository.save(event);
        log.debug("Save {} - {}", SIMPLE_NAME, eventInStorage);
        return eventInStorage;
    }
}
