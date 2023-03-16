package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    private String app;
    private String uri;
    private Long hits;
}
