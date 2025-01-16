package ru.yandex.practicum.service;


import ru.yandex.practicum.user.model.dto.CreateUserDto;
import ru.yandex.practicum.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(CreateUserDto createUserDto);

    List<UserDto> getAll(final List<Long> ids, final int from, final int size);

    UserDto getById(Long userId);

    void deleteById(final long id);
}
