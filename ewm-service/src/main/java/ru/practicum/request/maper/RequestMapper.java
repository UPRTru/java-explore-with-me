package ru.practicum.request.maper;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto requestToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(String.valueOf(LocalDateTime.parse(
                        request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .build();
    }

    public static Collection<RequestDto> requestsToDtoCollection(Collection<Request> requests) {
        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }
}
