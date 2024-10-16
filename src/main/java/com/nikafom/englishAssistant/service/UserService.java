package com.nikafom.englishAssistant.service;

import com.nikafom.englishAssistant.model.dto.request.UserInfoRequest;
import com.nikafom.englishAssistant.model.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    public UserInfoResponse createUser(@Valid UserInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        return UserInfoResponse.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public UserInfoResponse getUser(Long id) {
        return null;
    }

    public UserInfoResponse updateUser(Long id, @Valid UserInfoRequest request) {
        if(!request.getPhoneNumber().matches("^\\+79[0-9]{9}$")) {
            return null;
        }

        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        return UserInfoResponse.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public void deleteUser(Long id) {
    }

    public List<UserInfoResponse> getAllUsers() {
        return Collections.emptyList();
    }
}
