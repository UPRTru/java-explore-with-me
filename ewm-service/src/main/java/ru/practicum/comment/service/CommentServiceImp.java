package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.dto.CommentTextDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.comment.mapper.CommentMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto addComment(CommentShortDto commentShortDto) {
        if (commentShortDto.getText() == null || commentShortDto.getText().isEmpty()) {
            log.info("Текст комментария не должен быть пустым.");
            throw new ConflictException("Текст комментария не должен быть пустым.");
        }
        if (commentShortDto.getAuthorId() == null || commentShortDto.getEventId() == null) {
            log.info("Не указан id пользователя или события.");
            throw new ConflictException("Не указан id пользователя или события.");
        }
        User user;
        try {
            user =  userRepository.findById(commentShortDto.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Пользователь с id: "
                            + commentShortDto.getAuthorId() + " не найден."));
        } catch (NotFoundException e) {
            log.info("Пользователь с id: {} не найден.", commentShortDto.getAuthorId());
            throw new NotFoundException(e.getMessage());
        }
        Event event;
        try {
            event =  eventRepository.findById(commentShortDto.getEventId())
                    .orElseThrow(() -> new NotFoundException("Событие с id: "
                            + commentShortDto.getEventId() + " не найдено"));
        } catch (NotFoundException e) {
            log.info("Событие с id: {} не найдено", commentShortDto.getEventId());
            throw new NotFoundException(e.getMessage());
        }
        Comment comment = shortDtoToComment(commentShortDto, user, event, LocalDateTime.now());
        log.info("Добавлен новый отзыв в базу данных: {}", comment);
        return commentToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto updateCommentUserPrivate(Long userId, Long commentId, CommentTextDto commentTextDto) {
        Comment comment = checkComment(commentId, userId);
        if (commentTextDto.getText() != null && !commentTextDto.getText().equals("")) {
            comment.setText(commentTextDto.getText());
        }
        log.info("Отзыв обновлен. {}", comment);
        return commentToDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsUserAdmin(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.info("Пользователь с id: {} не найден.", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
        }
        log.info("Получение списка комментариев пользователя id: {}", userId);
        return commentsToDtoList(commentRepository.findAllByAuthorId(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDto getCommentAdmin(Long commentId) {
        Comment comment = checkComment(commentId, null);
        log.info("Получение комментария. {}", comment);
        return commentToDto(comment);
    }

    @Transactional
    @Override
    public void deleteCommentAdmin(Long commentId) {
        checkComment(commentId, null);
        log.info("Комментарий с id: {} удален.", commentId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    @Override
    public void deleteCommentPrivate(Long commentId, Long userId) {
        checkComment(commentId, userId);
        log.info("Комментарий id: {} пользователя id: {} удален.", commentId, userId);
        commentRepository.deleteByIdAndAuthorId(commentId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsPublic(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.info("Событие с id: {} не найдено", eventId);
            throw new NotFoundException("Событие с id: " + eventId + " не найдено");
        }
        log.info("Получение списка комментариев события id: {}", eventId);
        return commentsToDtoList(commentRepository.findAllByEventIdOrderByCreated(eventId));
    }

    private Comment checkComment(Long commentId, Long userId) {
        if (userId != null) {
            if (!userRepository.existsById(userId)) {
                log.info("Пользователь с id: {} не найден.", userId);
                throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
            }
            try {
                return commentRepository.findByIdAndAuthorId(commentId, userId)
                        .orElseThrow(() -> new NotFoundException("Комментарий id: " + commentId + " пользователя id: "
                                + userId + " не найден."));
            } catch (NotFoundException e) {
                log.info("Комментарий id: {} пользователя id: {} не найден.", commentId, userId);
                throw new NotFoundException(e.getMessage());
            }
        } else {
            try {
                return commentRepository.findById(commentId)
                        .orElseThrow(() -> new NotFoundException("Комментарий id: " + commentId + " не найден."));
            } catch (NotFoundException e) {
                log.info("Комментарий id: {} не найден.", commentId);
                throw new NotFoundException(e.getMessage());
            }

        }
    }
}
