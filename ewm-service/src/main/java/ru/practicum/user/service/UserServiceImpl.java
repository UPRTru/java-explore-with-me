package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static ru.practicum.user.mapper.UserMapper.dtoToUser;
import static ru.practicum.user.mapper.UserMapper.userToDtoCollection;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository usersRepository) {
        this.userRepository = usersRepository;
    }

    @Transactional
    @Override
    public void createUser(UserDto userDto) {
        userRepository.save(dtoToUser(userDto));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getUsers(List<Long> listId, Pageable pageable) {
        if (listId == null) {
            return userToDtoCollection(userRepository.findAll(pageable).toList());
        }
        return userToDtoCollection(userRepository.findAllByIdIn(listId, pageable).toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        userRepository.deleteById(userId);
    }
}
