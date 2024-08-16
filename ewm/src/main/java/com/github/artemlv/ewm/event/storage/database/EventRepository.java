package com.github.artemlv.ewm.event.storage.database;

import com.github.artemlv.ewm.event.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findByInitiatorId(final long userId, final Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(final long id, final long userId);

    List<Event> findByCategoryId(final long catId);

    Set<Event> findByIdIn(final Set<Long> ids);

    List<Event> findByLocationLatAndLocationLonAndLocationRadius(final double lat,
                                                                 final double lon,
                                                                 final double radius);

    List<Event> findByLocationLatAndLocationLon(final double lat, final double lon);
}
