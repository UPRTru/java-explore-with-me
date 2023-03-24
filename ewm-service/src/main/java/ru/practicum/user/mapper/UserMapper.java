package ru.practicum.user.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public static User dtoToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static List<UserDto> userToDtoCollection(Page<User> userPage) {
        return userPage.stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }
}
