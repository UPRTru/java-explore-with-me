package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto compilationToDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(),
                (List<EventShortDto>) EventMapper.eventsToShortDtoCollection(compilation.getEvents()),
                compilation.getPinned(), compilation.getTitle());
    }

    public static Compilation newDtoToCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(null, null, newCompilationDto.getPinned(), newCompilationDto.getTitle());
    }

    public static Collection<CompilationDto> compilationsToDtoCollections(Page<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::compilationToDto).collect(Collectors.toList());
    }
}
