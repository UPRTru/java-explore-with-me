package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;
import java.util.Set;

import static ru.practicum.compilation.mapper.CompilationMapper.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> findEvents = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Compilation compilation = newDtoToCompilation(newCompilationDto);
        compilation.setEvents(findEvents);
        log.info("Добавлена новая компиляция в базу данных: {}", compilation);
        return compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            log.info("Компиляция с id: {} не найдена.", compId);
            throw new NotFoundException("Компиляция с id: " + compId + " не найдена.");
        }
        log.info("Компиляция с id: {} удалена.", compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation) {
        Compilation compilation = getCompil(compId);
        Set<Event> findEvents = eventRepository.findAllByIdIn(updateCompilation.getEvents());
        if (updateCompilation.getPinned() != null && !updateCompilation.getPinned().equals(compilation.getPinned())) {
            compilation.setPinned(updateCompilation.getPinned());
        }
        if (updateCompilation.getTitle() != null && !updateCompilation.getTitle().isEmpty()
                && !updateCompilation.getTitle().equals(compilation.getTitle())) {
            compilation.setTitle(updateCompilation.getTitle());
        }
        compilation.setEvents(findEvents);
        log.info("Компиляция {} обновлена.", compilation);
        return compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        log.info("Получение компиляции id: {}", compId);
        return compilationToDto(getCompil(compId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        log.info("Получение списка компиляций.");
        if (pinned == null) {
            return compilationsToDtoCollections(compilationRepository.findAll(pageable));
        } else {
            return compilationsToDtoCollections(compilationRepository.findAllByPinned(pinned, pageable));
        }
    }

    private Compilation getCompil(Long compId) {
        try {
            return compilationRepository.findById(compId)
                    .orElseThrow(() -> new NotFoundException("Компиляция с id: " + compId + " не найдена."));
        } catch (NotFoundException e) {
            log.info("Компиляция с id: {} не найдена.", compId);
            throw new NotFoundException(e.getMessage());
        }
    }
}
