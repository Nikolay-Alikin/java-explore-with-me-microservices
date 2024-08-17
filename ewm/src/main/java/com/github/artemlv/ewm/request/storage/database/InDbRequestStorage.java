package com.github.artemlv.ewm.request.storage.database;


import com.github.artemlv.ewm.exception.type.NotFoundException;
import com.github.artemlv.ewm.request.model.Request;
import com.github.artemlv.ewm.request.storage.RequestStorage;
import com.github.artemlv.ewm.state.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbRequestStorage implements RequestStorage {
    private final RequestRepository requestRepository;
    private static final String SIMPLE_NAME = Request.class.getSimpleName();

    @Override
    public int countByEventIdAndStatus(final long id, final State state) {
        final int count = requestRepository.countByEventIdAndStatus(id, state);
        log.info("countByEventIdAndStatus count: {}", count);
        return count;
    }

    @Override
    @Transactional
    public Request save(Request request) {
        final Request requestInStorage = requestRepository.save(request);
        log.info("Save {} - {}", SIMPLE_NAME, requestInStorage);
        return requestInStorage;
    }

    @Override
    public List<Request> findAllByRequesterId(final long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Getting all {} : {}", SIMPLE_NAME, requests);
        return requests;
    }

    @Override
    public Optional<Request> getById(final long id) {
        final Optional<Request> request = requestRepository.findById(id);
        log.info("Get Optional<{}> by id - {}", SIMPLE_NAME, id);
        return request;
    }

    @Override
    public Request getByIdOrElseThrow(final long id) {
        return getById(id).orElseThrow(() -> new NotFoundException(SIMPLE_NAME, id));
    }
}
