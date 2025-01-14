package ru.yandex.practicum.storage.database;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByNameContainingIgnoreCase(final String text, final PageRequest page);
}
