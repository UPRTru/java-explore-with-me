package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.dto.CommentTextDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentShortDto commentShortDto);

    CommentDto updateCommentUserPrivate(Long userId, Long commentId, CommentTextDto commentTextDto);

    List<CommentDto> getCommentsUserAdmin(Long userId);

    CommentDto getCommentAdmin(Long commentId);

    void deleteCommentAdmin(Long commentId);

    void deleteCommentPrivate(Long commentId, Long userId);

    List<CommentDto> getCommentsPublic(Long eventId);
}
