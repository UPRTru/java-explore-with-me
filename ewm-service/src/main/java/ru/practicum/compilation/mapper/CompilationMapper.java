package ru.practicum.compilation.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto compilationToDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events((List<EventShortDto>) EventMapper.eventsToShortDtoCollection(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation newDtoToCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static Collection<CompilationDto> compilationsToDtoCollections(Page<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::compilationToDto).collect(Collectors.toList());
    }
}
