package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

public interface UserService {
    UserDto getById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    void deleteUser(Long id);
}
