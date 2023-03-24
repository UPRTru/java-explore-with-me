package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    Collection<RequestDto> getUserRequests(Long userId);

    RequestDto canceledRequest(Long userId, Long eventId);
}
