package ru.practicum.request.maper;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto requestToDto(Request request) {
        return new RequestDto(request.getId(), request.getEvent().getId(), request.getRequester().getId(),
                request.getStatus(), request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static List<RequestDto> requestsSetToDtoList(Set<Request> requests) {
        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }

    public static List<RequestDto> requestsListToDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::requestToDto).collect(Collectors.toList());
    }
}
