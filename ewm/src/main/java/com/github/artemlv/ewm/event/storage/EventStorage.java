package com.github.artemlv.ewm.event.storage;

import com.github.artemlv.ewm.event.model.Event;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface EventStorage {
    Event save(Event event);

    List<Event> findAll(final BooleanExpression predicate, final PageRequest pageRequest);

    Optional<Event> getById(final long id);

    Event getByIdOrElseThrow(final long id);

    List<Event> findAllByInitiatorId(final long userId, final PageRequest pageRequest);

    List<Event> findAllByLocationAndRadius(final double lat, final double lon, final double radius);

    List<Event> findAllEventsByLocation(final double lat, final double lon);
}
