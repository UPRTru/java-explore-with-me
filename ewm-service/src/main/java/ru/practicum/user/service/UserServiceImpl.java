package ru.practicum.user.service;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserListDto;
import ru.practicum.user.model.QUser;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.user.mapper.UserMapper.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository usersRepository) {
        this.userRepository = usersRepository;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        if (!userRepository.findAllByEmail(userDto.getEmail()).isEmpty()) {
            log.info("Емейл занят. {}", userDto.getEmail());
            throw new ConflictException("Емейл занят. " + userDto.getEmail());
        }
        User user = dtoToUser(userDto);
        log.info("Добавлен новый пользователь в базу данных. {}", user);
        return userToDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    @Override
    public UserListDto getUsers(List<Long> ids, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QUser.user.id.in(ids));
        }
        Page<User> page;
        if (booleanBuilder.getValue() != null) {
            page = userRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        log.info("Получение списка пользователей id: {}.", ids);
        return UserListDto.builder()
                .users(userToDtoCollection(page))
                .build();
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.findById(userId);
        } catch (Exception e) {
            log.info("Пользователь с id: {} не найден.", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
        }
        log.info("Удаление пользователя id: {}", userId);
        userRepository.deleteById(userId);
    }
}
