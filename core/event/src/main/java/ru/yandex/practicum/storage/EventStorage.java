package ru.yandex.practicum.storage;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ru.yandex.practicum.event.model.Event;

public interface EventStorage {

    Event save(Event event);

    List<Event> findAll(final BooleanExpression predicate, final PageRequest pageRequest);

    Optional<Event> getById(final long id);

    Event getByIdOrElseThrow(final long id);

    List<Event> findAllByInitiatorId(final long userId, final PageRequest pageRequest);

    List<Event> findAllByLocationAndRadius(final double lat, final double lon, final double radius);

    List<Event> findAllEventsByLocation(final double lat, final double lon);

    List<Event> findAll(final Specification<Event> spec, final PageRequest pageRequest);

    void saveAll(final List<Event> lists);
}
