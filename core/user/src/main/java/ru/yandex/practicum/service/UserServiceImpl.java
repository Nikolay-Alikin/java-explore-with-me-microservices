package ru.yandex.practicum.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.storage.UserStorage;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.model.dto.CreateUserDto;
import ru.yandex.practicum.user.model.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final UserStorage userStorage;

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        User user = cs.convert(createUserDto, User.class);
        return cs.convert(userStorage.save(user), UserDto.class);
    }

    @Override
    public List<UserDto> getAll(final List<Long> ids, final int from, final int size) {
        return userStorage.getAll(ids, from, size).stream()
                .map(user -> cs.convert(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto getById(Long userId) {
        var user = userStorage.getByIdOrElseThrow(userId);
        return cs.convert(user, UserDto.class);
    }

    @Override
    public void deleteById(final long id) {
        userStorage.existsByIdOrElseThrow(id);
        userStorage.deleteById(id);
    }
}
