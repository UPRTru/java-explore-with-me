package ru.practicum.compilation.service;

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

@Service
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImp(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> findEvents = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Compilation compilation = newDtoToCompilation(newCompilationDto);
        compilation.setEvents(findEvents);
        return compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id: " + compId + " не найдена."));
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id: " + compId + " не найдена."));
        Set<Event> findEvents = eventRepository.findAllByIdIn(updateCompilation.getEvents());
        if (updateCompilation.getPinned() != null && !updateCompilation.getPinned().equals(compilation.getPinned())) {
            compilation.setPinned(updateCompilation.getPinned());
        }
        if (updateCompilation.getTitle() != null && !updateCompilation.getTitle().isEmpty()
                && !updateCompilation.getTitle().equals(compilation.getTitle())) {
            compilation.setTitle(updateCompilation.getTitle());
        }
        compilation.setEvents(findEvents);
        return compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        return compilationToDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id: " + compId + " не найдена.")));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationsToDtoCollections(compilationRepository.findAll(pageable));
        } else {
            return compilationsToDtoCollections(compilationRepository.findAllByPinned(pinned, pageable));
        }
    }
}
