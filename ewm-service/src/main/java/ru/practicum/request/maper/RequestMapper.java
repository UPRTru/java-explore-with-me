package ru.practicum.request.maper;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto requestToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static List<RequestDto> requestsSetToDtoList(Set<Request> requests) {
        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }

    public static List<RequestDto> requestsListToDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }
}
