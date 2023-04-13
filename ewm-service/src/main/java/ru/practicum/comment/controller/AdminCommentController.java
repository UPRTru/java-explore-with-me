package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@Validated
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    //Просмотр администратором конкретного комментария
    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentAdmin(@PathVariable Long commentId) {
        return commentService.getCommentAdmin(commentId);
    }

    //Просмотр администратором все комментарии пользователя
    @GetMapping("user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsUserAdmin(@PathVariable Long userId) {
        return commentService.getCommentsUserAdmin(userId);
    }

    //Удаление комментария администратором
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteCommentAdmin(@PathVariable Long commentId) {
        commentService.deleteCommentAdmin(commentId);
        return "Комментарий с id: " + commentId + " был удален.";
    }
}
