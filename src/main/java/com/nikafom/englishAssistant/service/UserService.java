package com.nikafom.englishAssistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikafom.englishAssistant.exceptions.CustomException;
import com.nikafom.englishAssistant.model.db.entity.User;
import com.nikafom.englishAssistant.model.db.repository.UserRepository;
import com.nikafom.englishAssistant.model.dto.request.UserInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.UserInfoResponse;
import com.nikafom.englishAssistant.model.enums.UserStatus;
import com.nikafom.englishAssistant.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
        validatePhoneNumber(request);
        userRepository.findByPhoneNumber(request.getPhoneNumber())
                        .ifPresent(user -> {
                            throw new CustomException(String.format("User with phone number: %s already exists", request.getPhoneNumber()), HttpStatus.BAD_REQUEST);
                        });

        validateEmail(request);
        userRepository.findByEmailIgnoreCase(request.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(String.format("User with email: %s already exists", request.getEmail()), HttpStatus.BAD_REQUEST);
                });

        User user = mapper.convertValue(request, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.CREATED);

        User savedUser = userRepository.save(user);

        return mapper.convertValue(savedUser, UserInfoResponse.class);
    }

    private void validatePhoneNumber(UserInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            throw new CustomException("Invalid phone number format", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmail(UserInfoRequest request) {
        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            throw new CustomException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
    }

    public UserInfoResponse getUser(Long id) {
        User user = getUserFromDB(id);
        return mapper.convertValue(user, UserInfoResponse.class);
    }

    public User getUserFromDB(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    public UserInfoResponse updateUser(Long id, @Valid UserInfoRequest request) {
        validatePhoneNumber(request);
        validateEmail(request);

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

    public Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<User> allUsers;
        if(filter == null) {
            allUsers = userRepository.findAllByStatusNot(pageRequest, UserStatus.DELETED);
        } else {
            allUsers = userRepository.findAllByStatusNotFiltered(pageRequest, UserStatus.DELETED, filter.toUpperCase());
        }

        List<UserInfoResponse> content = allUsers.getContent().stream()
                .map(user -> mapper.convertValue(user, UserInfoResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, allUsers.getTotalElements());
    }
}
