package ru.practicum.user.service;

import com.querydsl.core.BooleanBuilder;
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
            throw new ConflictException("Емейл занят.");
        }
        return userToDto(userRepository.save(dtoToUser(userDto)));
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
        return UserListDto
                .builder()
                .users(userToDtoCollection(page))
                .build();
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        checkUser(userId);
        userRepository.deleteById(userId);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
    }
}
