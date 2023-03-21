package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserListDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserListDto getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);
}
