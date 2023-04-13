package ru.practicum.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static Comment shortDtoToComment(CommentShortDto commentShortDto, User user, Event event,
                                            LocalDateTime create) {
        return new Comment(null, commentShortDto.getText(), create, user, event);
    }

    public static CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getCreated(),
                comment.getAuthor().getId(), comment.getEvent().getId());
    }

    public static List<CommentDto> commentsToDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::commentToDto).collect(Collectors.toList());
    }
}
