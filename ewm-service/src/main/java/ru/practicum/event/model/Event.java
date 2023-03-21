package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.enums.State;
import ru.practicum.event.location.Location;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 1000)
    String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;
    @Column(nullable = false, length = 4000)
    String description;
    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;
    @Embedded
    Location location;
    @Column
    Boolean paid = Boolean.FALSE;
    @Column(name = "participant_limit")
    Integer participantLimit = 0;
    @Column(name = "request_moderation")
    Boolean requestModeration = Boolean.TRUE;
    @Column(nullable = false, length = 120)
    String title;
    @Column(name = "confirmed_requests")
    Integer confirmedRequests = 0;
    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    User initiator;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column
    String state = State.PENDING.name();
    @Column
    Long views = 0L;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    Set<Request> requests;
    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    Set<Compilation> compilations;
}
