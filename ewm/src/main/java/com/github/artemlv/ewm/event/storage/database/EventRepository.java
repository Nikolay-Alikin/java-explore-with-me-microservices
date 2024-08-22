package com.github.artemlv.ewm.event.storage.database;

import com.github.artemlv.ewm.event.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event>,
        JpaSpecificationExecutor<Event> {

    List<Event> findByInitiatorId(final long userId, final Pageable pageable);

    List<Event> findByCategoryId(final long catId);

    List<Event> findByLocationLatAndLocationLonAndLocationRadius(final double lat,
                                                                 final double lon,
                                                                 final double radius);

    List<Event> findByLocationLatAndLocationLon(final double lat, final double lon);
}
