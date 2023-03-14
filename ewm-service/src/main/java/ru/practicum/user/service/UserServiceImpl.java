package ru.practicum.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.Conflict;
import ru.practicum.exception.NotFound;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import static ru.practicum.user.dto.UserMapper.dtoToUser;
import static ru.practicum.user.dto.UserMapper.userToDto;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFound("Пользователь с id: " + id + " не найден."));
        return userToDto(user);
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        return userToDto(userRepository.save(dtoToUser(userDto)));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, Long id) {
        User updatedUser = userRepository.findById(id).orElseThrow(()
                -> new NotFound("Пользователь id: " + id + " не найден."));
        if (userDto.getEmail() != null && !userDto.getEmail().equals("")) {
            if (!userRepository.findAllByIdNotAndEmail(id, userDto.getEmail()).isEmpty()) {
                throw new Conflict("Email занят.");
            }
            updatedUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().equals("")) {
            updatedUser.setName(userDto.getName());
        }
        return userToDto(userRepository.save(updatedUser));
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFound("Пользователь с id: " + id + " не найден."));
        userRepository.delete(user);
    }
}
