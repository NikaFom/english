package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.model.db.entity.User;
import com.nikafom.englishAssistant.model.db.repository.UserRepository;
import com.nikafom.englishAssistant.model.dto.request.UserInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.UserInfoResponse;
import com.nikafom.englishAssistant.model.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final ObjectMapper mapper;
    private final UserRepository userRepository;

    public UserInfoResponse createUser(@Valid UserInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        User user = mapper.convertValue(request, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.CREATED);

        User savedUser = userRepository.save(user);

        return mapper.convertValue(savedUser, UserInfoResponse.class);
    }

    public UserInfoResponse getUser(Long id) {
        User user = getUserFromDB(id);
        return mapper.convertValue(user, UserInfoResponse.class);
    }

    public User getUserFromDB(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public UserInfoResponse updateUser(Long id, @Valid UserInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        User user = getUserFromDB(id);

        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword() == null ? user.getPassword() : request.getPassword());

        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.UPDATED);

        User savedUser = userRepository.save(user);

        return mapper.convertValue(savedUser, UserInfoResponse.class);
    }

    public void deleteUser(Long id) {
        User user = getUserFromDB(id);
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    public List<UserInfoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapper.convertValue(user, UserInfoResponse.class))
                .collect(Collectors.toList());
    }
}
