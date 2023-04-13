package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByAuthorId(Long userId);

    void deleteByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByEventIdOrderByCreated(Long eventId);
}
