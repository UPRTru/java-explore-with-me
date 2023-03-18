package ru.practicum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hits")
@NamedNativeQuery(name = "findUnique",
        query = "SELECT h.app, h.uri, count(DISTINCT h.ip) AS quantity " +
                "FROM hits AS h " +
                "WHERE h.uri IN ?1 AND h.timestamp >= ?2 AND h.timestamp <= ?3 " +
                "GROUP BY h.app, h.uri ORDER BY quantity DESC",
        resultSetMapping = "statsMapping")
@NamedNativeQuery(name = "findStats",
        query = "SELECT h.app, h.uri, count(h.ip) AS quantity " +
                "FROM hits AS h " +
                "WHERE h.uri IN ?1 AND h.timestamp >= ?2 AND h.timestamp <= ?3 " +
                "GROUP BY h.app, h.uri ORDER BY quantity DESC",
        resultSetMapping = "statsMapping")
@SqlResultSetMapping(name = "statsMapping",
        classes = {@ConstructorResult(
                targetClass = Stats.class,
                columns = {
                @ColumnResult(name = "app", type = String.class),
                @ColumnResult(name = "uri", type = String.class),
                @ColumnResult(name = "quantity", type = Long.class)})
        })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 50)
    String app;
    @Column(nullable = false, length = 256)
    String uri;
    @Column(nullable = false, length = 24)
    String ip;
    @Column(nullable = false)
    LocalDateTime timestamp;
}
