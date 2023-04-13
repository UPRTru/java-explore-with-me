package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.dto.CommentTextDto;
import ru.practicum.comment.service.CommentService;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class PrivateCommentController {
    private final CommentService commentService;

    //Создание уомментария пользователем
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestBody @Validated CommentShortDto commentShortDto) {
        return commentService.addComment(commentShortDto);
    }

    //Редактирование комментария пользователем
    @PatchMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentUserPrivate(@PathVariable Long userId, @PathVariable Long commentId,
                                               @RequestBody @Validated CommentTextDto commentTextDto) {
        return commentService.updateCommentUserPrivate(userId, commentId, commentTextDto);
    }

    //Удаление комментария пользователем
    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteCommentPrivate(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteCommentPrivate(commentId, userId);
        return "Комментарий с id: " + commentId + " был удален.";
    }
}
