package ru.practicum.user.service;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.QUser;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Collection;
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
    public Collection<UserDto> getUsers(List<Long> listId, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (listId != null && !listId.isEmpty()) {
            booleanBuilder.and(QUser.user.id.in(listId));
        }
        Page<User> page;
        if (booleanBuilder.getValue() != null) {
            page = userRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        return userToDtoCollection(page.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userRepository.deleteById(userId);
    }
}
