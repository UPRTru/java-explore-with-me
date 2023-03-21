package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {
    public static User dtoToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static Collection<UserDto> userToDtoCollection(Collection<User> userPage) {
        return userPage.stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }
}
