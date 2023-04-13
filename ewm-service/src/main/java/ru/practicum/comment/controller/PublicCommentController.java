package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Validated
@RequiredArgsConstructor
public class PublicCommentController {
    private final CommentService commentService;

    //Публичный просмотр комментариев события
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsPublic(@PathVariable Long eventId) {
        return commentService.getCommentsPublic(eventId);
    }
}
