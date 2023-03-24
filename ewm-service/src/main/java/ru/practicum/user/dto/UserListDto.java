package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserListDto {
    @JsonValue
    private List<UserDto> users;
}
