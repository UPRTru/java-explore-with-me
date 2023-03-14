package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.Stats;
import ru.practicum.service.StaticService;

import java.util.List;

@Slf4j
@RestController
public class StaticController {
    private final StaticService staticService;

    public StaticController(StaticService staticService) {
        this.staticService = staticService;
    }

    @GetMapping("stats")
    public List<Stats> getStats(@RequestParam String start,
                                @RequestParam String end,
                                @RequestParam(required = false) List<String> uris,
                                @RequestParam(defaultValue = "false") Boolean uniq) {
        return staticService.getStats(start, end, uris, uniq);
    }

    @PostMapping("/hit")
    public void saveHit(@RequestBody HitDto hitDto) {
        staticService.saveHit(hitDto);
    }
}
