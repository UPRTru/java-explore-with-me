package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    void createUser(UserDto userDto);

    Collection<UserDto> getUsers(List<Long> listId, Pageable pageable);

    void deleteUser(Long userId);
}
