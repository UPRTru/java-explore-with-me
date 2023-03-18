package ru.practicum.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Validated
public class PrivateControllerRequest {
    private final RequestService requestService;

    public PrivateControllerRequest(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRequest(@PathVariable @Min(1) Long userId, @RequestParam @Min(1) Long eventId) {
        requestService.createRequest(userId, eventId);
    }

    @GetMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestDto> getUserRequests(@PathVariable @Min(1) Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long requestId) {
        return requestService.canceledRequest(userId, requestId);
    }
}
