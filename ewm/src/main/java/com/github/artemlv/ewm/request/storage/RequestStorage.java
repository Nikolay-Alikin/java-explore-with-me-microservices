package com.github.artemlv.ewm.request.storage;

import com.github.artemlv.ewm.request.model.Request;
import com.github.artemlv.ewm.state.State;

import java.util.List;
import java.util.Optional;

public interface RequestStorage {
    int countByEventIdAndStatus(final long id, final State state);

    Request save(final Request request);

    List<Request> findAllByRequesterId(final long userId);

    Optional<Request> getById(final long id);

    Request getByIdOrElseThrow(final long id);
}
