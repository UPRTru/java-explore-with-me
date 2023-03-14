package ru.practicum.user.dto;

import ru.practicum.user.model.User;

public class UserMapper {
    public static UserDtoName userToDtoName(User user) {
        return UserDtoName
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserDto userToDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User dtoToUser(UserDto userDto) {
        return User
                .builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
